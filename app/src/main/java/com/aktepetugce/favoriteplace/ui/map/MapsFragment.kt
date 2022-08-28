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
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel =
            ViewModelProvider(this).get(MapsViewModel::class.java)
        subscribeToObservers()
        val place = args.place
        place?.let {
            mUIPlace = it
        }
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
    }

    private fun subscribeToObservers() {
        viewModel.isLoading.observe(viewLifecycleOwner, Observer {
            if (it) {
                binding.progressBar.visibility = View.VISIBLE
            } else {
                binding.progressBar.visibility = View.GONE
            }
        })
        viewModel.isSaveSuccess.observe(viewLifecycleOwner, Observer {
            Toast.makeText(requireContext(), "Success", Toast.LENGTH_LONG).show()
            findNavController().popBackStack(R.id.navigation_home, false)
        })
        viewModel.error.observe(viewLifecycleOwner, Observer {
            Toast.makeText(requireContext(), it.toString(), Toast.LENGTH_LONG).show()
        })
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
        mGoogleMap.addMarker(MarkerOptions().title("New Place").position(latLng))
        Toast.makeText(requireContext(), "Click On Save", Toast.LENGTH_LONG).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        locationManager.removeUpdates(locationListener)
    }
}