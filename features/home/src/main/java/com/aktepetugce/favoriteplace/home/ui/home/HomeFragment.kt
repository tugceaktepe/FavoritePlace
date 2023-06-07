package com.aktepetugce.favoriteplace.home.ui.home

import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import androidx.recyclerview.widget.DividerItemDecoration
import com.aktepetugce.favoriteplace.core.extension.gone
import com.aktepetugce.favoriteplace.core.extension.hide
import com.aktepetugce.favoriteplace.core.extension.launchAndCollectIn
import com.aktepetugce.favoriteplace.core.extension.show
import com.aktepetugce.favoriteplace.core.extension.showSnackbar
import com.aktepetugce.favoriteplace.core.extension.visible
import com.aktepetugce.favoriteplace.domain.model.Place
import com.aktepetugce.favoriteplace.home.R
import com.aktepetugce.favoriteplace.home.databinding.FragmentHomeBinding
import com.aktepetugce.favoriteplace.home.ui.home.adapter.PlaceRecyclerAdapter
import com.aktepetugce.favoriteplace.uicomponents.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment :
    BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate),
    MenuProvider {

    private val viewModel: HomeViewModel by viewModels()
    private val placeRecyclerAdapter: PlaceRecyclerAdapter by lazy {
        PlaceRecyclerAdapter()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeObservers()
        initializeUI()

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun initializeUI() {
        with(binding) {
            recyclerViewLocations.adapter = placeRecyclerAdapter
            recyclerViewLocations.addItemDecoration(
                (
                    DividerItemDecoration(
                        requireContext(),
                        DividerItemDecoration.VERTICAL
                    )
                    )
            )
            swipeRefreshLayout.setOnRefreshListener {
                viewModel.fetchPlaces(isLoading = false)
                swipeRefreshLayout.isRefreshing = false
            }
        }
        placeRecyclerAdapter.setOnItemClickListener { placeItem ->
            navigatePlaceToDetail(placeItem)
        }
    }

    private fun navigatePlaceToDetail(placeItem: Place) {
        val bundle = Bundle()
        bundle.putParcelable("place", placeItem)
        findNavController().navigate(R.id.action_fragmentHome_to_fragmentDetail, bundle)
    }

    private fun subscribeObservers() {
        with(binding) {
            viewModel.uiState.launchAndCollectIn(viewLifecycleOwner) { uiState ->
                when (uiState) {
                    is HomeUiState.InitialState -> viewModel.fetchPlaces()
                    is HomeUiState.Loading -> progressBar.show()
                    is HomeUiState.Error -> {
                        if (uiState.isNotShown) {
                            progressBar.hide()
                            requireView().showSnackbar(uiState.message)
                            viewModel.errorMessageShown()
                        }
                    }

                    is HomeUiState.PlaceListLoaded -> {
                        progressBar.hide()
                        if (uiState.isEmpty()) {
                            binding.textViewEmptyList.visible()
                        } else {
                            binding.textViewEmptyList.gone()
                            placeRecyclerAdapter.submitList(uiState.placeList)
                        }
                    }

                    is HomeUiState.UserSignedOut -> {
                        progressBar.hide()
                        navigateToLogin()
                    }
                }
            }
        }
    }

    private fun navigateToLogin() {
        val deepLinkUri = Uri.parse("android-app:/com.aktepetugce.favoriteplace/authentication/login")
        findNavController().navigate(
            deepLinkUri,
            navOptions {
                popUpTo(R.id.fragmentHome) { inclusive = true }
            }
        )
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.home_menu, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return when (menuItem.itemId) {
            R.id.log_out -> {
                viewModel.signOut()
                true
            }

            else -> false
        }
    }

    override fun onDestroyView() {
        binding.recyclerViewLocations.adapter = null
        super.onDestroyView()
    }
}
