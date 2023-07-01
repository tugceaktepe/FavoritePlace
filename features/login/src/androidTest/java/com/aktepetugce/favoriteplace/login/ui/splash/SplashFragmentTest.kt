package com.aktepetugce.favoriteplace.login.ui.splash

import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.aktepetugce.favoriteplace.login.R
import com.aktepetugce.favoriteplace.testing.ui.BaseFragmentTest
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.test.assertEquals

@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class SplashFragmentTest : BaseFragmentTest(){

    @Test
    fun screenIsReady() {
        launch<SplashFragment>(
            graphId = R.navigation.login_navigation
        )
        Espresso.onView(ViewMatchers.withId(R.id.imageViewSplash))
            .check(ViewAssertions.matches(isDisplayed()))

        val backStack = testNavHostController.backStack
        val currentDestination = backStack.last()

        assertEquals(currentDestination.destination.id, R.id.fragmentLogin)
    }
}