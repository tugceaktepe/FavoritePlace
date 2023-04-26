package com.aktepetugce.favoriteplace.ui.detail

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.navArgs
import com.aktepetugce.favoriteplace.R
import com.aktepetugce.favoriteplace.base.BaseFragment
import com.aktepetugce.favoriteplace.databinding.FragmentDetailBinding
import com.aktepetugce.favoriteplace.di.GlideApp
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailFragment : BaseFragment<FragmentDetailBinding>(FragmentDetailBinding::inflate, false), OnMapReadyCallback {

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
                textViewName.text = place.placeName
                typeTextView.text = place.placeType
                GlideApp.with(requireContext())
                    .load(place.placeImageUrl)
                    .into(imageViewPlace)
            }
            mGoogleMap.clear()
            if (place.placeLatitude != 0.0 && place.placeLongitude != 0.0) {
                val placeLocation = LatLng(place.placeLatitude, place.placeLongitude)
                mGoogleMap.addMarker(MarkerOptions().position(placeLocation).title(place.placeName))
                mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(placeLocation, ZOOM_OPTION))
            }
        }
    }

    companion object {
        const val ZOOM_OPTION = 15f
    }
}
