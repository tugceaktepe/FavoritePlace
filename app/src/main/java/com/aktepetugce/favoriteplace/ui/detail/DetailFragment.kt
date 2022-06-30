package com.aktepetugce.favoriteplace.ui.detail

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.aktepetugce.favoriteplace.R
import com.aktepetugce.favoriteplace.databinding.FragmentDetailBinding
import com.bumptech.glide.RequestManager
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import javax.inject.Inject

class DetailFragment @Inject constructor(
    private val glide : RequestManager
) : Fragment(R.layout.fragment_detail) , OnMapReadyCallback {

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: DetailViewModel
    private val args: DetailFragmentArgs by navArgs()
    private lateinit var mGoogleMap : GoogleMap

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(DetailViewModel::class.java)
        val mapFragment = childFragmentManager.findFragmentById(R.id.mapDetail) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mGoogleMap = googleMap
        getLocationData()
    }

    private fun getLocationData(){
        val place = args.place
        place?.let { place ->
            with(binding){
                nameTextView.text = place.placeName
                typeTextView.text = place.placeType
                glide.load(place.placeImageUrl).into(placeImageView)
            }
            mGoogleMap.clear()
            if(place.placeLatitude != 0.0 && place.placeLongitude != 0.0){
                val placeLocation = LatLng(place.placeLatitude, place.placeLongitude)
                mGoogleMap.addMarker(MarkerOptions().position(placeLocation).title(place.placeName))
                mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(placeLocation, 15f))
            }
        }
    }

}