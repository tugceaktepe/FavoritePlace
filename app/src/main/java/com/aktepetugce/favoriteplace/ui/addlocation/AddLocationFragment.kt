package com.aktepetugce.favoriteplace.ui.addlocation

import android.app.Activity
import android.content.Intent
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.aktepetugce.favoriteplace.R
import com.aktepetugce.favoriteplace.base.BaseFragment
import com.aktepetugce.favoriteplace.common.extension.onClick
import com.aktepetugce.favoriteplace.databinding.FragmentAddLocationBinding
import com.aktepetugce.favoriteplace.domain.model.UIPlace
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddLocationFragment : BaseFragment<FragmentAddLocationBinding>(FragmentAddLocationBinding::inflate, false) {
    private val viewModel: AddLocationViewModel by viewModels()
    private lateinit var selectedImageUri: Uri

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            imageViewLocation.setOnClickListener {
                imageViewLocation.onClick {
                    val intent = Intent(Intent.ACTION_PICK)
                    intent.type = "image/*"
                    activityLauncher.launch(intent)
                }
            }

            buttonNext.onClick {
                if (editTextLocationType.text.isNullOrEmpty() || editTextName.text.isNullOrEmpty()) {
                    showErrorMessage(getString(R.string.name_or_type_empty_error))
                } else {
                    val uiPlace = UIPlace(
                        placeUserEmail = viewModel.currentUserEmail,
                        placeName = editTextName.text.toString(),
                        placeImage = if (this@AddLocationFragment::selectedImageUri.isInitialized) {
                            selectedImageUri
                        } else {
                            null
                        },
                        placeType = editTextLocationType.text.toString(),
                        placeAtmosphere = editTextAtmosphere.text.toString(),
                        placeImageUrl = "",
                        placeLatitude = 0.0,
                        placeLongitude = 0.0
                    )
                    val action =
                        AddLocationFragmentDirections.actionFragmentAddLocationToFragmentMaps(
                            uiPlace
                        )
                    hideKeyboard()
                    findNavController().navigate(action)
                }
            }
        }
    }

    private val activityLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                selectedImageUri = result.data?.data!!
                val selectedImageBitMap = when {
                    Build.VERSION.SDK_INT < Build.VERSION_CODES.Q -> MediaStore.Images.Media.getBitmap(
                        requireContext().contentResolver,
                        selectedImageUri
                    )
                    else -> {
                        val source =
                            ImageDecoder.createSource(requireContext().contentResolver, selectedImageUri)
                        ImageDecoder.decodeBitmap(source)
                    }
                }
                binding.imageViewLocation.setImageBitmap(selectedImageBitMap)
            }
        }
}
