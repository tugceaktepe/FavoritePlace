package com.aktepetugce.favoriteplace.ui.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.view.menu.MenuBuilder
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.aktepetugce.favoriteplace.R
import com.aktepetugce.favoriteplace.base.BaseFragment
import com.aktepetugce.favoriteplace.databinding.FragmentHomeBinding
import com.aktepetugce.favoriteplace.ui.home.adapter.PlaceRecyclerAdapter
import com.aktepetugce.favoriteplace.util.extension.launchAndCollectIn
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment :
    BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate, hasOptionsMenu = true) {

    private val viewModel: HomeViewModel by viewModels()
    private val placeRecyclerAdapter: PlaceRecyclerAdapter by lazy {
        PlaceRecyclerAdapter()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (viewModel.uiState.value.isUserAuthenticated) {
            subscribeObservers()
            binding.recyclerViewLocations.adapter = placeRecyclerAdapter
            binding.recyclerViewLocations.addItemDecoration(
                (
                    DividerItemDecoration(
                        requireContext(),
                        DividerItemDecoration.VERTICAL
                    )
                    )
            )
            placeRecyclerAdapter.setOnItemClickListener { position ->
                viewModel.uiState.value.placeList?.let {
                    val action = HomeFragmentDirections.actionHomeToDetailFragment(it[position])
                    findNavController().navigate(action)
                }
            }
            if (!viewModel.uiState.value.placesLoaded) {
                viewModel.fetchPlaces()
            }
        } else {
            findNavController().navigate(R.id.action_home_to_login)
        }
    }

    private fun subscribeObservers() {
        viewModel.uiState.launchAndCollectIn(viewLifecycleOwner) { uiState ->
            binding.progressBar.isVisible = uiState.isLoading
            uiState.errorMessage?.let {
                showErrorMessage(it)
                viewModel.userMessageShown()
            }
            if (uiState.placesLoaded) {
                placeRecyclerAdapter.submitList(uiState.placeList)
            }
            if (uiState.signOutSuccess) {
                findNavController().navigate(R.id.action_home_to_login)
            }
        }
    }

    @SuppressLint("RestrictedApi")
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.home_menu, menu)
        if (menu is MenuBuilder) {
            menu.setOptionalIconsVisible(true)
        }
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.log_out) {
            viewModel.signOut()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroyView() {
        binding.recyclerViewLocations.adapter = null
        super.onDestroyView()
    }
}
