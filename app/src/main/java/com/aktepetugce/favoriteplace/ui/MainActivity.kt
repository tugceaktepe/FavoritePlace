package com.aktepetugce.favoriteplace.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.aktepetugce.favoriteplace.R
import com.aktepetugce.favoriteplace.common.util.gone
import com.aktepetugce.favoriteplace.common.util.visible
import com.aktepetugce.favoriteplace.databinding.ActivityMainBinding
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
                R.id.fragmentSplash,
                R.id.fragmentLogin,
                R.id.fragmentRegister,
                R.id.fragmentHome,
                R.id.fragmentAddLocation
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
        manageBottomNavigation(destination.id)
        manageActionBar(destination.id)
    }

    private fun manageBottomNavigation(destinationId: Int) {
        when (destinationId) {
            R.id.fragmentSplash,
            R.id.fragmentLogin,
            R.id.fragmentRegister,
            R.id.fragmentForgotPassword,
            R.id.fragmentMaps,
            R.id.fragmentAddLocation -> {
                hideBottomNav()
            }

            else -> {
                showBottomNav()
            }
        }
    }

    private fun manageActionBar(destinationId: Int) {
        when (destinationId) {
            R.id.fragmentSplash,
            R.id.fragmentLogin,
            R.id.fragmentRegister,
            R.id.fragmentForgotPassword -> {
                hideActionBar()
            }

            else -> {
                showActionBar()
            }
        }
    }

    private fun showBottomNav() {
        binding.bottomNavigationView.visible()
    }

    private fun hideBottomNav() {
        binding.bottomNavigationView.gone()
    }

    private fun showActionBar() {
        binding.toolbar.visible()
    }

    private fun hideActionBar() {
        binding.toolbar.gone()
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
