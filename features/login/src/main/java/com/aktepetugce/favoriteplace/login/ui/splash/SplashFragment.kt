package com.aktepetugce.favoriteplace.login.ui.splash

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.aktepetugce.favoriteplace.core.util.TestIdlingResource
import com.aktepetugce.favoriteplace.login.R
import com.aktepetugce.favoriteplace.login.databinding.FragmentSplashBinding
import com.aktepetugce.favoriteplace.uicomponents.base.BaseFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashFragment :
    BaseFragment<FragmentSplashBinding>(FragmentSplashBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        CoroutineScope(Dispatchers.Main).launch {
            TestIdlingResource.increment()
            delay(DELAY_TIME)
            TestIdlingResource.decrement()
            findNavController().navigate(R.id.action_splashFragment_to_fragmentLogin)
        }
    }

    companion object {
        const val DELAY_TIME = 2000L
    }
}
