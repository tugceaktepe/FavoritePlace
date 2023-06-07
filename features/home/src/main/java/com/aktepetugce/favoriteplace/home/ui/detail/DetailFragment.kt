package com.aktepetugce.favoriteplace.home.ui.detail

import android.os.Bundle
import android.view.View
import com.aktepetugce.favoriteplace.core.di.GlideApp
import com.aktepetugce.favoriteplace.domain.model.Place
import com.aktepetugce.favoriteplace.home.R
import com.aktepetugce.favoriteplace.home.databinding.FragmentDetailBinding
import com.aktepetugce.favoriteplace.uicomponents.base.BaseFragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailFragment :
    BaseFragment<FragmentDetailBinding>(
        FragmentDetailBinding::inflate
    ),
    OnMapReadyCallback {

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
        val place = arguments?.getParcelable("place") as Place?
        place?.let {
            with(binding) {
                textViewName.text = it.name
                GlideApp.with(requireContext())
                    .load(it.imageUrl)
                    .into(imageViewPlace)
                textViewDescription.text = it.description
            }
            mGoogleMap.clear()
            if (it.latitude != 0.0 && it.longitude != 0.0) {
                val placeLocation = LatLng(it.latitude, it.longitude)
                mGoogleMap.addMarker(MarkerOptions().position(placeLocation).title(it.name))
                mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(placeLocation, ZOOM_OPTION))
            }
        }
    }

    companion object {
        const val ZOOM_OPTION = 15f
    }
}
