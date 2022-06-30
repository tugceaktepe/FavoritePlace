package com.aktepetugce.favoriteplace.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
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

    companion object{
        private const val TAG = "MainActivity"
    }
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController : NavController
    private lateinit var bottomNav : BottomNavigationView

    @Inject lateinit var baseFragmentFactory: BaseFragmentFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportFragmentManager.fragmentFactory = baseFragmentFactory
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupNav()
        Log.d(TAG,"onCreate")
    }

    private fun setupNav() {
        bottomNav = binding.navView

        navController = findNavController(R.id.nav_host_fragment_activity_main)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_add_location
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        bottomNav.setupWithNavController(navController)

        val destinationsWithNoBottomNav = listOf(R.id.navigation_add_location, R.id.navigation_maps)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            if(destinationsWithNoBottomNav.contains(destination.id)){
                hideBottomNav()
            }else{
                showBottomNav()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG,"onPause")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG,"onResume")
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG,"onStart")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG,"onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG,"onDestroy")
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
}