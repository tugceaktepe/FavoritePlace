package com.aktepetugce.favoriteplace.home.ui.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.view.menu.MenuBuilder
import androidx.core.net.toUri
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import androidx.recyclerview.widget.DividerItemDecoration
import com.aktepetugce.favoriteplace.common.base.BaseFragment
import com.aktepetugce.favoriteplace.common.extension.launchAndCollectIn
import com.aktepetugce.favoriteplace.home.R
import com.aktepetugce.favoriteplace.home.databinding.FragmentHomeBinding
import com.aktepetugce.favoriteplace.home.ui.home.adapter.PlaceRecyclerAdapter
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
        viewModel.fetchPlaces()
        subscribeObservers()
        prepareRecyclerView()
    }

    private fun prepareRecyclerView() {
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
                val action = HomeFragmentDirections.actionFragmentHomeToFragmentDetail(it[position])
                findNavController().navigate(action)
            }
        }
    }

    private fun subscribeObservers() {
        viewModel.uiState.launchAndCollectIn(viewLifecycleOwner) { uiState ->
            binding.progressBar.isVisible = uiState.isLoading
            uiState.errorMessage?.let {
                // TODO: Convert it one-shot event
                showErrorMessage(it)
                viewModel.userMessageShown()
            } ?: run {
                if (uiState.placesLoaded) {
                    placeRecyclerAdapter.submitList(uiState.placeList)
                } else if (uiState.signOutSuccess) {
                    navigateToLogin()
                }
            }
        }
    }

    private fun navigateToLogin() {
        val deepLinkUri = NavDeepLinkRequest.Builder
            .fromUri("android-app:/com.aktepetugce.favoriteplace/authentication/login".toUri())
            .build()
        findNavController().navigate(
            deepLinkUri,
            navOptions {
                popUpTo(R.id.fragmentHome) { inclusive = true }
            }
        )
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
}
