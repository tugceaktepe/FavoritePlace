package com.aktepetugce.favoriteplace.ui.map

import android.Manifest
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.view.menu.MenuBuilder
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.aktepetugce.favoriteplace.R
import com.aktepetugce.favoriteplace.base.BaseFragment
import com.aktepetugce.favoriteplace.databinding.FragmentMapsBinding
import com.aktepetugce.favoriteplace.domain.uimodel.UIPlace
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MapsFragment :
    BaseFragment<FragmentMapsBinding>(FragmentMapsBinding::inflate, hasOptionsMenu = true),
    OnMapReadyCallback, GoogleMap.OnMapLongClickListener {

    private val args: MapsFragmentArgs by navArgs()
    private lateinit var viewModel: MapsViewModel

    private lateinit var locationManager: LocationManager
    private lateinit var mGoogleMap: GoogleMap

    private var latitude = 0.0
    private var longitude = 0.0
    private lateinit var mUIPlace: UIPlace

    private val locationListener = LocationListener { location ->
        val sharedPref: SharedPreferences? = activity?.getPreferences(Context.MODE_PRIVATE)
        val firstTimeCheck = sharedPref?.getBoolean("firstTimeCheck", false)
        if (firstTimeCheck != null) {
            val newLocation = LatLng(location.latitude, location.longitude)
            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(newLocation, 15f))
            sharedPref.edit().putBoolean("firstTimeCheck", true).apply()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val place = args.place
        place?.let {
            mUIPlace = it
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(MapsViewModel::class.java)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
        subscribeObservers()
    }

    private fun subscribeObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { uiState ->
                    binding.progressBar.isVisible = uiState.isLoading
                    uiState.errorMessage?.let {
                        showErrorMessage(it)
                        viewModel.userMessageShown()
                    }
                    uiState.nextDestination?.let { nextPage ->
                        findNavController().navigate(nextPage)
                    }
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.maps_menu, menu)
        if (menu is MenuBuilder) {
            menu.setOptionalIconsVisible(true)
        }
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.save_location) {
            mUIPlace.placeLatitude = latitude
            mUIPlace.placeLongitude = longitude
            viewModel.savePlace(mUIPlace)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mGoogleMap = googleMap
        googleMap.setOnMapLongClickListener(this)
        locationManager = activity?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
        } else {
            locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                0L,
                0f,
                locationListener
            )
            mGoogleMap.clear()
            val lastKnownLocation =
                locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            if (lastKnownLocation != null) {
                val lastUserLocation =
                    LatLng(lastKnownLocation.latitude, lastKnownLocation.longitude)
                mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lastUserLocation, 15f))
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == 1 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            mGoogleMap.clear()
            val lastKnownLocation =
                locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            if (lastKnownLocation != null) {
                val lastUserLocation =
                    LatLng(lastKnownLocation.latitude, lastKnownLocation.longitude)
                mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lastUserLocation, 15f))
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onMapLongClick(latLng: LatLng) {
        longitude = latLng.longitude
        latitude = latLng.latitude
        mGoogleMap.addMarker(MarkerOptions().title(getString(R.string.map_marker_title)).position(latLng))
        Toast.makeText(requireContext(), getString(R.string.save_location_info), Toast.LENGTH_LONG).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        //TODO: Fix memory leaks
        locationManager.removeUpdates(locationListener)
    }
}