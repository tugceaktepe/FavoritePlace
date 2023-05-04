package com.aktepetugce.favoriteplace.ui.authentication.splash

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.aktepetugce.favoriteplace.R
import com.aktepetugce.favoriteplace.base.BaseFragment
import com.aktepetugce.favoriteplace.databinding.FragmentSplashBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashFragment :
    BaseFragment<FragmentSplashBinding>(FragmentSplashBinding::inflate, false) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        CoroutineScope(Dispatchers.Main).launch {
            delay(DELAY_TIME)
            findNavController().navigate(R.id.action_splashFragment_to_fragmentLogin)
        }
    }
    companion object {
        const val DELAY_TIME = 2000L
    }
}
