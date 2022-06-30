package com.aktepetugce.favoriteplace.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.view.menu.MenuBuilder
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.aktepetugce.favoriteplace.R
import com.aktepetugce.favoriteplace.databinding.FragmentHomeBinding
import com.aktepetugce.favoriteplace.ui.home.adapter.PlaceRecyclerAdapter
import com.aktepetugce.favoriteplace.ui.login.LoginActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment @Inject constructor(
    private val placeRecyclerAdapter : PlaceRecyclerAdapter
) : Fragment(R.layout.fragment_home) {

private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel : HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        subscribeToObservers()
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recylerViewLocations.adapter = placeRecyclerAdapter
        binding.recylerViewLocations.addItemDecoration((DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)))
        placeRecyclerAdapter.setOnItemClickListener { position ->
            val place = viewModel.placesList.value?.get(position)
            val action = HomeFragmentDirections.actionNavigationHomeToDetailFragment(place)
            findNavController().navigate(action)
        }
        val userEmail = viewModel.currentUserEmail
        viewModel.fetchPlaces(userEmail)
    }

    private fun subscribeToObservers(){
        viewModel.isLoading.observe(viewLifecycleOwner , Observer {
            if(it){
                binding.progressBar.visibility = View.VISIBLE
            }else{
                binding.progressBar.visibility = View.GONE
            }
        })
        viewModel.isSignOutSuccess.observe(viewLifecycleOwner , Observer {
            val intent = Intent(activity, LoginActivity::class.java)
            activity?.startActivity(intent)
            activity?.finish()
        })
        viewModel.error.observe(viewLifecycleOwner , Observer {
            Toast.makeText(requireContext(),it.toString(), Toast.LENGTH_LONG).show()
        })
        viewModel.placesList.observe(viewLifecycleOwner , Observer {
            (binding.recylerViewLocations.adapter as? PlaceRecyclerAdapter)?.submitList(it)
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.home_menu, menu )
        if(menu is MenuBuilder){
            menu.setOptionalIconsVisible(true)
        }
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.log_out){
            viewModel.signOut()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}