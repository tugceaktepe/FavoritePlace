package com.aktepetugce.favoriteplace.location.ui.addlocation

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.fragment.findNavController
import com.aktepetugce.favoriteplace.common.extension.onClick
import com.aktepetugce.favoriteplace.common.extension.showSnackbar
import com.aktepetugce.favoriteplace.common.util.BitmapResolver
import com.aktepetugce.favoriteplace.location.R
import com.aktepetugce.favoriteplace.location.databinding.FragmentAddLocationBinding
import com.aktepetugce.favoriteplace.location.domain.model.MapsArgs
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddLocationFragment :
    com.aktepetugce.favoriteplace.common.ui.BaseFragment<FragmentAddLocationBinding>(
        FragmentAddLocationBinding::inflate
    ) {
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
                    requireView().showSnackbar(getString(R.string.name_or_type_empty_error))
                } else {
                    // TODO: UI state for input
                    val uri = if (this@AddLocationFragment::selectedImageUri.isInitialized) {
                        selectedImageUri
                    } else {
                        null
                    }
                    val action =
                        AddLocationFragmentDirections.actionFragmentAddLocationToFragmentMaps(
                            homeDestinationId = findNavController().previousBackStackEntry?.destination?.id ?: -1,
                            args = MapsArgs(editTextName.text.toString(), uri.toString()),
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
                val selectedImageBitMap =
                    BitmapResolver.getBitmap(requireContext().contentResolver, selectedImageUri)
                binding.imageViewLocation.setImageBitmap(selectedImageBitMap)
            }
        }
}
