package com.aktepetugce.favoriteplace.location.ui.map

import android.Manifest
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.LocationListener
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.navOptions
import com.aktepetugce.favoriteplace.common.R.drawable.ic_launcher_background
import com.aktepetugce.favoriteplace.common.base.BaseFragment
import com.aktepetugce.favoriteplace.common.domain.model.Place
import com.aktepetugce.favoriteplace.common.extension.launchAndCollectIn
import com.aktepetugce.favoriteplace.location.R
import com.aktepetugce.favoriteplace.location.databinding.FragmentMapsBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MapsFragment :
    BaseFragment<FragmentMapsBinding>(FragmentMapsBinding::inflate),
    OnMapReadyCallback,
    GoogleMap.OnMapLongClickListener,
    MenuProvider {

    private val args: MapsFragmentArgs by navArgs()
    private lateinit var viewModel: MapsViewModel

    private lateinit var locationManager: LocationManager
    private lateinit var mGoogleMap: GoogleMap

    private var latitude = 0.0
    private var longitude = 0.0
    private lateinit var name: String
    private lateinit var uri: Uri

    private val locationListener = LocationListener { location ->
        val sharedPref: SharedPreferences? = activity?.getPreferences(Context.MODE_PRIVATE)
        val firstTimeCheck = sharedPref?.getBoolean("firstTimeCheck", false)
        if (firstTimeCheck != null) {
            val newLocation = LatLng(location.latitude, location.longitude)
            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(newLocation, ZOOM_OPTION))
            sharedPref.edit().putBoolean("firstTimeCheck", true).apply()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        args.let {
            name = it.args.name
            uri = Uri.parse(it.args.uri)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(MapsViewModel::class.java)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)
        subscribeObservers()
    }

    private fun subscribeObservers() {
        viewModel.uiState.launchAndCollectIn(viewLifecycleOwner) { uiState ->
            binding.progressBar.isVisible = uiState.isLoading
            uiState.errorMessage?.let {
                showErrorMessage(it)
                viewModel.userMessageShown()
            } ?: run {
                if (uiState.success) {
                    navigateToHome()
                }
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mGoogleMap = googleMap
        googleMap.setOnMapLongClickListener(this)
        locationManager = activity?.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                0L,
                0f,
                locationListener
            )
            initializeLocation()
        } else {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            initializeLocation()
        }
    }

    private fun initializeLocation() {
        mGoogleMap.clear()
        val lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        if (lastKnownLocation != null) {
            val lastUserLocation =
                LatLng(lastKnownLocation.latitude, lastKnownLocation.longitude)
            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lastUserLocation, ZOOM_OPTION))
        }
    }

    override fun onMapLongClick(latLng: LatLng) {
        longitude = latLng.longitude
        latitude = latLng.latitude
        mGoogleMap.addMarker(MarkerOptions().title(getString(R.string.map_marker_title)).position(latLng))
        Toast.makeText(requireContext(), getString(R.string.save_location_info), Toast.LENGTH_LONG).show()
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

    override fun onDestroyView() {
        super.onDestroyView()
        // Fix memory leaks
        locationManager.removeUpdates(locationListener)
    }

    companion object {
        const val ZOOM_OPTION = 15f
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
                    feeling = Pair(2, ic_launcher_background),
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
}
