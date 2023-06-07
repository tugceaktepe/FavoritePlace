package com.aktepetugce.favoriteplace.login.ui.splash

import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.aktepetugce.favoriteplace.login.R
import com.aktepetugce.favoriteplace.testing.ui.BaseFragmentTest
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.every
import io.mockk.verify
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class SplashFragmentTest : BaseFragmentTest(){

    @Test
    fun screenIsReady() {
        every { navController.navigate(R.id.action_splashFragment_to_fragmentLogin) } returns Unit
        launch<SplashFragment>()
        Espresso.onView(ViewMatchers.withId(R.id.imageViewSplash))
            .check(ViewAssertions.matches(isDisplayed()))
        verify {
            navController.navigate(R.id.action_splashFragment_to_fragmentLogin)
        }
    }
}