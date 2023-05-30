package com.aktepetugce.favoriteplace.testing.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.test.espresso.IdlingRegistry
import com.aktepetugce.favoriteplace.core.util.TestIdlingResource
import com.aktepetugce.favoriteplace.testing.util.launchFragmentInHiltContainer
import dagger.hilt.android.testing.HiltAndroidRule
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.mockito.Mockito.mock
import org.mockito.MockitoAnnotations

open class BaseFragmentTest {
    @get:Rule
    internal val hiltRule by lazy { HiltAndroidRule(this) }

    // val testNavHostController = TestNavHostController(getApplicationContext())
    val navController: NavController = mock(NavController::class.java)

    @Before
    internal fun setup() {
        MockitoAnnotations.openMocks(this)
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
