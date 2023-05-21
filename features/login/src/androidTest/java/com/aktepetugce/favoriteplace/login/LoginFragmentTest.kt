package com.aktepetugce.favoriteplace.login

import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.aktepetugce.favoriteplace.login.ui.login.LoginFragment
import com.aktepetugce.favoriteplace.testing.ui.BaseFragmentTest
import com.aktepetugce.favoriteplace.uicomponents.R.style.FavoritePlaceTheme
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class LoginFragmentTest : BaseFragmentTest(){

    override fun setupTest() {
        launch<LoginFragment>(
            graphResId = R.navigation.login_navigation,
            destinationId = R.id.fragmentLogin,
            themeResId = FavoritePlaceTheme,
        )
    }

    @Test
    fun screenIsReady() {
        Espresso.onView(withId(R.id.welcomeTextView))
            .check(ViewAssertions.matches(ViewMatchers.withText(R.string.welcome_text)))
        Espresso.onView(withId(R.id.loginTextView))
            .check(ViewAssertions.matches(ViewMatchers.withText(R.string.sign_in)))
        Espresso.onView(withId(R.id.editTextEmail))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(withId(R.id.editTextPassword))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(withId(R.id.buttonSignIn))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(withId(R.id.textViewSignUp))
            .check(ViewAssertions.matches(ViewMatchers.withText(R.string.sign_up_text)))
    }
}