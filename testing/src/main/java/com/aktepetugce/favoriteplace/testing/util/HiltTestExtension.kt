package com.aktepetugce.favoriteplace.testing.util

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import androidx.core.util.Preconditions
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import com.aktepetugce.favoriteplace.testing.HiltTestActivity
import kotlinx.coroutines.ExperimentalCoroutinesApi

@SuppressLint("RestrictedApi")
@ExperimentalCoroutinesApi
inline fun <reified T : Fragment> launchFragmentInHiltContainer(
    fragmentArgs: Bundle? = null,
    navController: NavController,
    themeResId: Int = androidx.fragment.testing.R.style.FragmentScenarioEmptyFragmentActivityTheme,
    crossinline action: Fragment.() -> Unit = {}
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
                Navigation.setViewNavController(fragment.requireView(), navController)
            }
        }
        fragment.arguments = fragmentArgs
        activity.supportFragmentManager.beginTransaction()
            .add(android.R.id.content, fragment, "")
            .commitNow()

        fragment.action()
    }
}
