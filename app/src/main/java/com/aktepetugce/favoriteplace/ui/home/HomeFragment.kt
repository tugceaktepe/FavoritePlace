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
import com.aktepetugce.favoriteplace.common.extension.launchAndCollectIn
import com.aktepetugce.favoriteplace.databinding.FragmentHomeBinding
import com.aktepetugce.favoriteplace.ui.home.adapter.PlaceRecyclerAdapter
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
        findNavController().navigate(R.id.action_fragmentHome_to_login_navigation)
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
