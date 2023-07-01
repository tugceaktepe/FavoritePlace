package com.aktepetugce.favoriteplace.testing.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.espresso.IdlingRegistry
import com.aktepetugce.favoriteplace.core.util.TestIdlingResource
import com.aktepetugce.favoriteplace.testing.util.HiltContainerConfig
import com.aktepetugce.favoriteplace.testing.util.launchFragmentInHiltContainer
import dagger.hilt.android.testing.HiltAndroidRule
import org.junit.After
import org.junit.Before
import org.junit.Rule

open class BaseFragmentTest {
    @get:Rule
    internal val hiltRule by lazy { HiltAndroidRule(this) }

    val testNavHostController = TestNavHostController(getApplicationContext())

    @Before
    internal fun setup() {
        IdlingRegistry.getInstance().register(TestIdlingResource.countingIdlingResource)
        hiltRule.inject()
    }

    protected inline fun <reified F : Fragment> launch(
        fragmentArgs: Bundle? = null,
        graphId: Int = 0,
        currentDestination: Int = 0,
        crossinline action: Fragment.() -> Unit = {}
    ) {
        launchFragmentInHiltContainer<F>(
            HiltContainerConfig(
                fragmentArgs = fragmentArgs,
                navController = testNavHostController,
                navResId = graphId,
                currentDestination = currentDestination
            )
        ) {
            action(this)
        }
    }

    @After
    internal fun tearDown() {
        IdlingRegistry.getInstance().unregister(TestIdlingResource.countingIdlingResource)
    }
}
