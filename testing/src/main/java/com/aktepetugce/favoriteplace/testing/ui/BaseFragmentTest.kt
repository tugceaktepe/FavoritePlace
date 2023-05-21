package com.aktepetugce.favoriteplace.testing.ui

import android.os.Bundle
import androidx.annotation.NavigationRes
import androidx.annotation.StyleRes
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import com.aktepetugce.favoriteplace.testing.util.launchFragmentInHiltContainer
import dagger.hilt.android.testing.HiltAndroidRule
import org.junit.After
import org.junit.Before
import org.junit.Rule

abstract class BaseFragmentTest {
    @get:Rule
    internal val hiltRule by lazy { HiltAndroidRule(this) }

    @Before
    internal fun setup() {
        hiltRule.inject()
        setupTest()
    }

    abstract fun setupTest()

    protected inline fun <reified F : Fragment> launch(
        fragmentArgs: Bundle? = null,
        @NavigationRes graphResId: Int = -1,
        destinationId: Int,
        @StyleRes themeResId: Int = -1,
    ) {
        launchFragmentInHiltContainer<F>(
            fragmentArgs = fragmentArgs,
            themeResId = themeResId,
        ) {
            val navigation = TestNavHostController(getApplicationContext())
            navigation.setGraph(graphResId)
            navigation.setCurrentDestination(destinationId)
            Navigation.setViewNavController(requireView(), navigation)
        }
    }

    @After
    internal fun tearDown() {
        clearTest()
    }

    open fun clearTest() { }
}
