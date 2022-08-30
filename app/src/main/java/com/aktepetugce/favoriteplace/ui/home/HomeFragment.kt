package com.aktepetugce.favoriteplace.ui.home

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.view.menu.MenuBuilder
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.aktepetugce.favoriteplace.R
import com.aktepetugce.favoriteplace.base.BaseFragment
import com.aktepetugce.favoriteplace.databinding.FragmentHomeBinding
import com.aktepetugce.favoriteplace.ui.home.adapter.PlaceRecyclerAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment @Inject constructor(
    private val placeRecyclerAdapter: PlaceRecyclerAdapter
) : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate, hasOptionsMenu = true) {

    private lateinit var viewModel: HomeViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        if (viewModel.uiState.value.isUserAuthenticated) {
            subscribeObservers()
            binding.recylerViewLocations.adapter = placeRecyclerAdapter
            binding.recylerViewLocations.addItemDecoration(
                (DividerItemDecoration(
                    requireContext(),
                    DividerItemDecoration.VERTICAL
                ))
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
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { uiState ->
                    binding.progressBar.isVisible = uiState.isLoading
                    uiState.errorMessage?.let {
                        showErrorMessage(it)
                        viewModel.userMessageShown()
                    }
                    if (uiState.placesLoaded) {
                        placeRecyclerAdapter.submitList(uiState.placeList)
                    }
                    if(uiState.signOutSuccess){
                        findNavController().navigate(R.id.action_home_to_login)
                    }
                }
            }
        }
    }

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