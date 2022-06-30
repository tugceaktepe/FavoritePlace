package com.aktepetugce.favoriteplace.base

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.aktepetugce.favoriteplace.ui.detail.DetailFragment
import com.aktepetugce.favoriteplace.ui.home.HomeFragment
import com.aktepetugce.favoriteplace.ui.home.adapter.PlaceRecyclerAdapter
import com.bumptech.glide.RequestManager
import javax.inject.Inject

class BaseFragmentFactory @Inject constructor(
    private val placeRecyclerAdapter: PlaceRecyclerAdapter,
    private val glide : RequestManager
) : FragmentFactory() {
    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
        return when(className){
            HomeFragment::class.java.name -> HomeFragment(placeRecyclerAdapter)
            DetailFragment::class.java.name -> DetailFragment(glide)
            else -> super.instantiate(classLoader, className)
        }
    }
}