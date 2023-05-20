package com.aktepetugce.favoriteplace.location.ui.map

import android.Manifest
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.core.view.get
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.navOptions
import com.aktepetugce.favoriteplace.core.extension.gone
import com.aktepetugce.favoriteplace.core.extension.launchAndCollectIn
import com.aktepetugce.favoriteplace.core.extension.showSnackbar
import com.aktepetugce.favoriteplace.core.extension.visible
import com.aktepetugce.favoriteplace.domain.model.Place
import com.aktepetugce.favoriteplace.location.R
import com.aktepetugce.favoriteplace.location.databinding.FragmentMapsBinding
import com.aktepetugce.favoriteplace.uicomponents.base.BaseFragment
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MapsFragment :
    BaseFragment<FragmentMapsBinding>(FragmentMapsBinding::inflate),
    OnMapReadyCallback,
    GoogleMap.OnMapLongClickListener,
    MenuProvider {

    private val args: MapsFragmentArgs by navArgs()
    private val viewModel: MapsViewModel by viewModels()

    private var _map: GoogleMap? = null
    private val map get() = _map!!

    private var _fusedLocationClient: FusedLocationProviderClient? = null
    private val fusedLocationClient get() = _fusedLocationClient!!

    private var previousLocationMarker: Marker? = null

    private var _menuHost: MenuHost? = null
    private val menuHost get() = _menuHost!!

    private var latitude = 0.0
    private var longitude = 0.0
    private lateinit var name: String
    private lateinit var uri: Uri
    private var saveIconEnabled = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        args.let {
            name = it.args.name
            uri = Uri.parse(it.args.uri)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
        _fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())

        setSaveButton()
        subscribeObservers()
    }

    private fun setSaveButton() {
        _menuHost = requireActivity()
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun subscribeObservers() {
        with(binding) {
            viewModel.uiState.launchAndCollectIn(viewLifecycleOwner) { uiState ->
                when (uiState) {
                    is MapsUiState.InitialState -> {}
                    is MapsUiState.Loading -> progressBar.visible()
                    is MapsUiState.Error -> {
                        progressBar.gone()
                        requireView().showSnackbar(uiState.message)
                        viewModel.uiState
                    }

                    is MapsUiState.LocationIsAdded -> {
                        progressBar.gone()
                        navigateToHome()
                    }
                }
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        _map = googleMap
        googleMap.setOnMapLongClickListener(this)
        googleMap.uiSettings.isZoomControlsEnabled = true
        setUpMap()
    }

    private fun setUpMap() {
        when {
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED -> {
                getDeviceLocation()
            }

            shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION) -> {
                AlertDialog.Builder(requireContext())
                    .setTitle("Konum izni gerekli")
                    .setMessage("Konumunuzu kullanarak daha iyi bir deneyim sağlayabiliriz.")
                    .setPositiveButton("Tamam") { _, _ ->
                        requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                    }
                    .setNegativeButton("İptal") { dialog, _ ->
                        dialog.dismiss()
                    }
                    .create()
                    .show()
            }

            else -> {
                requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            }
        }
    }

    private fun getDeviceLocation() {
        try {
            map.isMyLocationEnabled = true
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                location?.let {
                    setCurrentLocation(location)
                } ?: run {
                    requestLocationUpdate()
                }
            }.addOnFailureListener { exception ->
                Log.d(TAG, exception.message ?: exception.toString())
            }
        } catch (e: SecurityException) {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    private fun setCurrentLocation(location: Location) {
        val currentLatLng = LatLng(location.latitude, location.longitude)
        placeMarkerOnMap(currentLatLng)
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, ZOOM_OPTION))
    }

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                getDeviceLocation()
            } else {
                Toast.makeText(requireContext(), "Konum izni reddedildi.", Toast.LENGTH_SHORT)
                    .show()
            }
        }

    private fun requestLocationUpdate() {
        val locationRequest = LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY,
            LOCATION_INTERVAL
        ).build()

        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                for (location in locationResult.locations) {
                    location?.let {
                        setCurrentLocation(location)
                        fusedLocationClient.removeLocationUpdates(this)
                    }
                }
            }
        }
        try {
            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                null
            )
        } catch (e: SecurityException) {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    private fun placeMarkerOnMap(location: LatLng) {
        val markerOptions = MarkerOptions().position(location)
        markerOptions.icon(
            BitmapDescriptorFactory.fromBitmap(
                BitmapFactory.decodeResource(resources, R.mipmap.ic_user_location)
            )
        )
        markerOptions.title(getString(R.string.current_location_marker_title))
        map.addMarker(markerOptions)
    }

    override fun onMapLongClick(place: LatLng) {
        previousLocationMarker?.remove()
        previousLocationMarker = null
        longitude = place.longitude
        latitude = place.latitude
        previousLocationMarker =
            map.addMarker(MarkerOptions().title(getString(R.string.marker_title)).position(place))
        if (!saveIconEnabled) {
            menuHost.invalidateMenu()
        }
    }

    private fun navigateToHome() {
        val deepLinkUri = NavDeepLinkRequest.Builder
            .fromUri("android-app:/com.aktepetugce.favoriteplace/home".toUri())
            .build()
        findNavController().navigate(
            deepLinkUri,
            navOptions {
                popUpTo(args.homeDestinationId) { inclusive = true }
            }
        )
    }

    companion object {
        const val TAG = "MapsFragment"
        const val ZOOM_OPTION = 15f
        const val LOCATION_INTERVAL = 10000L
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.maps_menu, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return when (menuItem.itemId) {
            R.id.save_location -> {
                // TODO: move this logic to viewmodel
                val place = Place(
                    name = name,
                    latitude = latitude,
                    longitude = longitude,
                    description = "",
                    feeling = Pair(2, -1),
                    id = "",
                    instanceId = 0,
                    imageUrl = ""
                )
                viewModel.savePlace(place, uri)
                true
            }

            else -> false
        }
    }

    override fun onPrepareMenu(menu: Menu) {
        if (previousLocationMarker == null) {
            menu[0].isEnabled = false
            menu[0].icon =
                AppCompatResources.getDrawable(requireContext(), R.drawable.ic_done_icon_passive)
        } else {
            menu[0].isEnabled = true
            menu[0].icon = AppCompatResources.getDrawable(requireContext(), R.drawable.ic_done_icon)
            saveIconEnabled = true
        }
        super.onPrepareMenu(menu)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _map = null
        _fusedLocationClient = null
        previousLocationMarker = null
    }
}
