package com.aktepetugce.favoriteplace.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.aktepetugce.favoriteplace.R
import com.aktepetugce.favoriteplace.databinding.ActivityMainBinding
import com.aktepetugce.favoriteplace.home.R.id.fragmentDetail
import com.aktepetugce.favoriteplace.home.R.id.fragmentHome
import com.aktepetugce.favoriteplace.location.R.id.fragmentAddLocation
import com.aktepetugce.favoriteplace.location.R.id.fragmentMaps
import com.aktepetugce.favoriteplace.login.R.id.fragmentForgotPassword
import com.aktepetugce.favoriteplace.login.R.id.fragmentLogin
import com.aktepetugce.favoriteplace.login.R.id.fragmentRegister
import com.aktepetugce.favoriteplace.login.R.id.fragmentSplash
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), NavController.OnDestinationChangedListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var appBarConfiguration: AppBarConfiguration
    override fun onCreate(savedInstanceState: Bundle?) {
        handleIntent()
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController
        val toolbar = binding.toolbar
        setSupportActionBar(toolbar)
        appBarConfiguration = AppBarConfiguration
            .Builder(
                fragmentSplash,
                fragmentLogin,
                fragmentRegister,
                fragmentHome,
                fragmentAddLocation
            )
            .build()
        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.bottomNavigationView.setupWithNavController(navController)
        navController.addOnDestinationChangedListener(this)
    }

    override fun onSupportNavigateUp(): Boolean {
        return findNavController(R.id.fragmentContainerView).navigateUp(appBarConfiguration)
    }

    override fun onDestinationChanged(
        controller: NavController,
        destination: NavDestination,
        arguments: Bundle?
    ) {
        if (destination.id == fragmentAddLocation) {
            controller.currentBackStackEntry?.savedStateHandle?.set(
                "HOME_DESTINATION_ID",
                controller.previousBackStackEntry?.destination?.id
            )
        }
        manageBottomNavigation(destination.id)
        manageActionBar(destination.id)
    }

    private fun manageBottomNavigation(destinationId: Int) {
        when (destinationId) {
            fragmentSplash,
            fragmentLogin,
            fragmentRegister,
            fragmentDetail,
            fragmentForgotPassword,
            fragmentMaps,
            fragmentAddLocation -> {
                hideBottomNav()
            }

            else -> {
                showBottomNav()
            }
        }
    }

    private fun manageActionBar(destinationId: Int) {
        when (destinationId) {
            fragmentSplash,
            fragmentLogin,
            fragmentRegister,
            fragmentForgotPassword -> {
                hideActionBar()
            }

            else -> {
                showActionBar()
            }
        }
    }

    private fun showBottomNav() {
        binding.bottomNavigationView.isVisible = true
    }

    private fun hideBottomNav() {
        binding.bottomNavigationView.isVisible = false
    }

    private fun showActionBar() {
        binding.toolbar.isVisible = true
    }

    private fun hideActionBar() {
        binding.toolbar.isVisible = true
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        handleIntent()
    }
    private fun handleIntent() {
        val appLinkAction = intent.action
        val appLinkData: Uri? = intent.data
        if (Intent.ACTION_VIEW == appLinkAction) {
            appLinkData?.lastPathSegment?.also { recipeId ->
                Uri.parse("content://tugceaktepe.github.io/favoriteplace/")
                    .buildUpon()
                    .appendPath(recipeId)
                    .build().also { appData ->
                        Log.d(TAG, "Appdata: $appData")
                    }
            }
        }
    }
    companion object {
        private const val TAG = "MainActivity"
    }
}
