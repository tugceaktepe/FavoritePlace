package com.aktepetugce.favoriteplace.testing.util

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Intent
import androidx.core.util.Preconditions
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelStore
import androidx.navigation.Navigation
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import com.aktepetugce.favoriteplace.testing.HiltTestActivity
import kotlinx.coroutines.ExperimentalCoroutinesApi

@SuppressLint("RestrictedApi")
@ExperimentalCoroutinesApi
inline fun <reified T : Fragment> launchFragmentInHiltContainer(
    config: HiltContainerConfig,
    themeResId: Int = androidx.fragment.testing.R.style.FragmentScenarioEmptyFragmentActivityTheme,
    crossinline action: Fragment.() -> Unit = {},
) {
    val mainActivityIntent = Intent.makeMainActivity(
        ComponentName(
            ApplicationProvider.getApplicationContext(),
            HiltTestActivity::class.java
        )
    ).putExtra("FragmentScenario.EmptyFragmentActivity.THEME_EXTRAS_BUNDLE_KEY", themeResId)

    ActivityScenario.launch<HiltTestActivity>(mainActivityIntent).onActivity { activity ->
        val fragment = activity.supportFragmentManager.fragmentFactory.instantiate(
            Preconditions.checkNotNull(T::class.java.classLoader),
            T::class.java.name
        )
        fragment.viewLifecycleOwnerLiveData.observeForever { viewLifecycleOwner ->
            if (viewLifecycleOwner != null) {
                config.navController.setViewModelStore(ViewModelStore())
                if (config.navResId != 0) {
                    config.navController.setGraph(config.navResId)
                }
                if (config.currentDestination != 0) {
                    config.navController.setCurrentDestination(config.currentDestination)
                }
                Navigation.setViewNavController(fragment.requireView(), config.navController)
            }
        }
        fragment.arguments = config.fragmentArgs
        activity.supportFragmentManager.beginTransaction()
            .add(android.R.id.content, fragment, "")
            .commitNow()

        fragment.action()
    }
}
