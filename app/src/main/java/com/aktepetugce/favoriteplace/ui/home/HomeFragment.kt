package com.aktepetugce.favoriteplace.ui.home

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.view.menu.MenuBuilder
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.aktepetugce.favoriteplace.R
import com.aktepetugce.favoriteplace.base.BaseFragment
import com.aktepetugce.favoriteplace.databinding.FragmentHomeBinding
import com.aktepetugce.favoriteplace.ui.home.adapter.PlaceRecyclerAdapter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment @Inject constructor(
    private val placeRecyclerAdapter: PlaceRecyclerAdapter
) : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate, hasOptionsMenu = true) {

    private lateinit var viewModel: HomeViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        if(viewModel.isUserAuthenticated){
            subscribeToObservers()
            binding.recylerViewLocations.adapter = placeRecyclerAdapter
            binding.recylerViewLocations.addItemDecoration(
                (DividerItemDecoration(
                    requireContext(),
                    DividerItemDecoration.VERTICAL
                ))
            )
            placeRecyclerAdapter.setOnItemClickListener { position ->
                val place = viewModel.placesList.value?.get(position)
                val action = HomeFragmentDirections.actionHomeToDetailFragment(place)
                findNavController().navigate(action)
            }
            val userEmail = viewModel.currentUserEmail
            viewModel.fetchPlaces(userEmail)
        }else{
            findNavController().navigate(R.id.action_home_to_login)
        }
    }

    private fun subscribeToObservers() {
        viewModel.isLoading.observe(viewLifecycleOwner, Observer {
            binding.progressBar.isVisible = it
        })
        viewModel.isSignOutSuccess.observe(viewLifecycleOwner, Observer {
            findNavController().navigate(R.id.action_home_to_login)
        })
        viewModel.error.observe(viewLifecycleOwner, Observer {
            showErrorMessage(it)
        })
        viewModel.placesList.observe(viewLifecycleOwner, Observer {
            (binding.recylerViewLocations.adapter as? PlaceRecyclerAdapter)?.submitList(it)
        })
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