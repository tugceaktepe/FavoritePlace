package com.aktepetugce.favoriteplace.location.ui.addlocation

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
import com.aktepetugce.favoriteplace.common.data.model.Place
import com.aktepetugce.favoriteplace.common.extension.onClick
import com.aktepetugce.favoriteplace.location.R
import com.aktepetugce.favoriteplace.location.databinding.FragmentAddLocationBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddLocationFragment : com.aktepetugce.favoriteplace.common.base.BaseFragment<FragmentAddLocationBinding>(
    FragmentAddLocationBinding::inflate,
    false
) {
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
                    val uiPlace = Place(
                        userEmail = viewModel.currentUserEmail,
                        name = editTextName.text.toString(),
                        type = editTextLocationType.text.toString(),
                        atmosphere = editTextAtmosphere.text.toString(),
                        imageUrl = "",
                        latitude = 0.0.toString(),
                        longitude = 0.0.toString()
                    )
                    val uri = if (this@AddLocationFragment::selectedImageUri.isInitialized) {
                        selectedImageUri
                    } else {
                        null
                    }
                    val action =
                        AddLocationFragmentDirections.actionFragmentAddLocationToFragmentMaps(
                            uiPlace,
                            uri.toString()
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
