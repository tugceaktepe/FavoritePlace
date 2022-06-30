package com.aktepetugce.favoriteplace.ui.addlocation

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.aktepetugce.favoriteplace.databinding.FragmentAddLocationBinding
import com.aktepetugce.favoriteplace.domain.uimodel.UIPlace
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class AddLocationFragment : Fragment() {

    private var _binding: FragmentAddLocationBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel : AddLocationViewModel
    private lateinit var selectedImageUri : Uri

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel =
            ViewModelProvider(this).get(AddLocationViewModel::class.java)

        _binding = FragmentAddLocationBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding){
            imageView.setOnClickListener {
                if(ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) !=
                    PackageManager.PERMISSION_GRANTED){
                    requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 1)
                }else{
                    val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    startActivityForResult(intent,2)
                }
            }

            nextButton.setOnClickListener {
                if(typeEditText.text.isNullOrEmpty() || nameEditText.text.isNullOrEmpty()){
                    Toast.makeText(requireContext(),"Name or Type cannot be empty!",Toast.LENGTH_LONG).show()
                }else{
                    val uiPlace = UIPlace(
                        placeUserEmail = viewModel.currentUserEmail,
                        placeName = nameEditText.text.toString(),
                        placeImage = if (this@AddLocationFragment::selectedImageUri.isInitialized) selectedImageUri else null,
                        placeType = typeEditText.text.toString(),
                        placeAtmosphere = atmosphereEditText.text.toString(),
                        placeImageUrl = "",
                        placeLatitude = 0.0,
                        placeLongitude = 0.0
                    )
                    val action =
                        AddLocationFragmentDirections.actionNavigationAddLocationToMapsFragment(
                            uiPlace
                        )
                    findNavController().navigate(action)
                }
            }
            activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if(requestCode == 1 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED ){
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent,2)
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == 2 && resultCode == Activity.RESULT_OK && data!= null){
            selectedImageUri = data.data!!
            try {
                val selectedImage = MediaStore.Images.Media.getBitmap(activity?.contentResolver, selectedImageUri)
                binding.imageView.setImageBitmap(selectedImage)
            }catch (e: Exception){
                e.printStackTrace()
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}