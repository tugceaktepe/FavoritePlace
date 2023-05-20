package com.aktepetugce.favoriteplace.location.ui.addlocation

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.aktepetugce.favoriteplace.core.extension.launchAndCollectIn
import com.aktepetugce.favoriteplace.core.extension.onClick
import com.aktepetugce.favoriteplace.core.extension.showSnackbar
import com.aktepetugce.favoriteplace.uicomponents.base.BaseFragment
import com.aktepetugce.favoriteplace.core.util.BitmapResolver
import com.aktepetugce.favoriteplace.domain.model.args.MapsArgs
import com.aktepetugce.favoriteplace.location.R
import com.aktepetugce.favoriteplace.location.databinding.FragmentAddLocationBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class AddLocationFragment :
    BaseFragment<FragmentAddLocationBinding>(
        FragmentAddLocationBinding::inflate
    ) {

    private val viewModel: AddLocationViewModel by viewModels()
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
                // TODO: add validations and other inputs
                val locationName = editTextName.text.toString()
                if (locationName.isNullOrEmpty()) {
                    requireView().showSnackbar(getString(R.string.name_or_type_empty_error))
                } else {
                    val action =
                        AddLocationFragmentDirections.actionFragmentAddLocationToFragmentMaps(
                            homeDestinationId = findNavController().previousBackStackEntry?.destination?.id
                                ?: -1,
                            args = MapsArgs(locationName, viewModel.getSelectedImageUri()),
                        )
                    hideKeyboard()
                    findNavController().navigate(action)
                }
            }
        }
        subscribeObservers()
    }

    private fun subscribeObservers() {
        with(binding) {
            viewModel.uiState.launchAndCollectIn(viewLifecycleOwner) { uiState ->
                lifecycleScope.launch {
                    if (uiState.selectedPhotoUri.toString() != "null") {
                        val selectedImageBitMap = withContext(Dispatchers.IO) {
                            BitmapResolver.getBitmap(
                                requireContext().contentResolver,
                                uiState.selectedPhotoUri
                            )
                        }
                        imageViewLocation.setImageBitmap(selectedImageBitMap)
                    } else {
                        imageViewLocation.setImageDrawable(
                            AppCompatResources.getDrawable(
                                requireContext(),
                                R.drawable.ic_image_search
                            )
                        )
                    }
                }
            }
        }
    }

    // TODO: write an extension or move this method to util functions
    private val activityLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val selectedImageUri = result.data?.data!!
                viewModel.saveSelectedPhoto(selectedImageUri)
            }
        }
}
