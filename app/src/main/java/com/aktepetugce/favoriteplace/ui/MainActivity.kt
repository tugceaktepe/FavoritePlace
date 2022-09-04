package com.aktepetugce.favoriteplace.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.aktepetugce.favoriteplace.R
import com.aktepetugce.favoriteplace.base.BaseFragmentFactory
import com.aktepetugce.favoriteplace.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController : NavController
    private lateinit var bottomNav : BottomNavigationView

    @Inject lateinit var baseFragmentFactory: BaseFragmentFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        handleIntent()
        super.onCreate(savedInstanceState)
        supportFragmentManager.fragmentFactory = baseFragmentFactory
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupNav()
    }

    private fun setupNav() {
        bottomNav = binding.bottomNavigationView

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
        navController = navHostFragment.navController

        val appBarConfiguration = AppBarConfiguration(
            setOf(R.id.home,
                  R.id.login,
                  R.id.add_location)
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        bottomNav.setupWithNavController(navController)

        val destinationsWithNoBottomNav = listOf(R.id.login, R.id.register, R.id.forgotPassword, R.id.add_location, R.id.maps)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            if(destinationsWithNoBottomNav.contains(destination.id)){
                hideBottomNav()
            }else{
                showBottomNav()
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    private fun showBottomNav() {
        bottomNav.visibility = View.VISIBLE
    }

    private fun hideBottomNav() {
        bottomNav.visibility = View.GONE
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        handleIntent()
    }
    private fun handleIntent(){
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
    companion object{
        private const val TAG = "MainActivity"
    }
}