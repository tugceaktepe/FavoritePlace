package com.aktepetugce.favoriteplace.testing.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.test.espresso.IdlingRegistry
import com.aktepetugce.favoriteplace.core.util.TestIdlingResource
import com.aktepetugce.favoriteplace.testing.util.launchFragmentInHiltContainer
import dagger.hilt.android.testing.HiltAndroidRule
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK
import org.junit.After
import org.junit.Before
import org.junit.Rule

open class BaseFragmentTest {
    @get:Rule
    internal val hiltRule by lazy { HiltAndroidRule(this) }

    // val testNavHostController = TestNavHostController(getApplicationContext())
    @MockK
    lateinit var navController: NavController

    @Before
    internal fun setup() {
        MockKAnnotations.init(this)
        IdlingRegistry.getInstance().register(TestIdlingResource.countingIdlingResource)
        hiltRule.inject()
    }

    protected inline fun <reified F : Fragment> launch(
        fragmentArgs: Bundle? = null,
        crossinline action: Fragment.() -> Unit = {}
    ) {
        launchFragmentInHiltContainer<F>(
            fragmentArgs = fragmentArgs,
            navController = navController
        ) {
            action(this)
        }
    }

    @After
    internal fun tearDown() {
        IdlingRegistry.getInstance().unregister(TestIdlingResource.countingIdlingResource)
    }
}
