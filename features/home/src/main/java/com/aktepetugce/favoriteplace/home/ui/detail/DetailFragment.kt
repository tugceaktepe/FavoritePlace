package com.aktepetugce.favoriteplace.home.ui.detail

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.navArgs
import com.aktepetugce.favoriteplace.common.di.GlideApp
import com.aktepetugce.favoriteplace.home.R
import com.aktepetugce.favoriteplace.home.databinding.FragmentDetailBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailFragment :
    com.aktepetugce.favoriteplace.common.ui.BaseFragment<FragmentDetailBinding>(
        FragmentDetailBinding::inflate
    ),
    OnMapReadyCallback {

    private val args: DetailFragmentArgs by navArgs()
    private lateinit var mGoogleMap: GoogleMap

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.mapDetail) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mGoogleMap = googleMap
        getLocationData()
    }

    private fun getLocationData() {
        val place = args.place
        place?.let { place ->
            with(binding) {
                textViewName.text = place.name
                // typeTextView.text = place.type
                GlideApp.with(requireContext())
                    .load(place.imageUrl)
                    .into(imageViewPlace)
            }
            mGoogleMap.clear()
            if (place.latitude != 0.0 && place.longitude != 0.0) {
                val placeLocation = LatLng(place.latitude, place.longitude)
                mGoogleMap.addMarker(MarkerOptions().position(placeLocation).title(place.name))
                mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(placeLocation, ZOOM_OPTION))
            }
        }
    }

    companion object {
        const val ZOOM_OPTION = 15f
    }
}
