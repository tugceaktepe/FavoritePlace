package com.aktepetugce.favoriteplace.login.ui.login

import android.net.Uri
import androidx.navigation.navOptions
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.aktepetugce.favoriteplace.login.R
import com.aktepetugce.favoriteplace.testing.repository.FakeAuthRepository
import com.aktepetugce.favoriteplace.testing.ui.BaseFragmentTest
import com.aktepetugce.favoriteplace.testing.util.constant.LoginConstants.HOME_URI
import com.aktepetugce.favoriteplace.testing.util.constant.LoginConstants.INVALID_TEST_EMAIL
import com.aktepetugce.favoriteplace.testing.util.constant.LoginConstants.SIGN_IN_ERROR
import com.aktepetugce.favoriteplace.testing.util.constant.LoginConstants.TEST_EMAIL
import com.aktepetugce.favoriteplace.testing.util.constant.LoginConstants.TEST_PASSWORD
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.test.assertEquals

@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class LoginFragmentTest : BaseFragmentTest() {

    @Test
    fun screenIsReady() {
        FakeAuthRepository.isAuthenticated = false
        launch<LoginFragment>()
        onView(withId(R.id.textViewWelcome))
            .check(matches(withText(R.string.welcome_text)))
        onView(withId(R.id.textViewSignIn))
            .check(matches(withText(R.string.sign_in)))
        onView(withId(R.id.editTextEmail))
            .check(matches(isDisplayed()))
        onView(withId(R.id.editTextPassword))
            .check(matches(isDisplayed()))
        onView(withId(R.id.buttonSignIn))
            .check(matches(isDisplayed()))
        onView(withId(R.id.textViewSignUp))
            .check(matches(withText(R.string.sign_up_text)))
    }

    @Test
    fun loginSuccessfully() {
        FakeAuthRepository.isAuthenticated = false

        launch<LoginFragment>(
            graphId = R.navigation.login_navigation,
            currentDestination = R.id.fragmentLogin
        )

        onView(withId(R.id.editTextEmail))
            .perform(typeText(TEST_EMAIL))
        onView(withId(R.id.editTextPassword))
            .perform(typeText(TEST_PASSWORD))
        onView(withId(R.id.buttonSignIn))
            .perform(click())

        //TODO: add assertion after solving navigation problem about deeplinks :
        //https://stackoverflow.com/questions/76326962/android-navigation-destination-that-matches-request-navdeeplinkrequest-uri-a
    }

    @Test
    fun userIsAlreadyAuthenticated() {
        FakeAuthRepository.isAuthenticated = true

        launch<LoginFragment>(
            graphId = R.navigation.login_navigation,
            currentDestination = R.id.fragmentLogin
        )

        onView(withId(R.id.textViewWelcome))
            .check(matches(withText(R.string.welcome_text)))

        //TODO: add assertion after solving navigation problem about deeplinks :
        //https://stackoverflow.com/questions/76326962/android-navigation-destination-that-matches-request-navdeeplinkrequest-uri-a
    }

    @Test
    fun loginWithError() {
        FakeAuthRepository.isAuthenticated = false

        launch<LoginFragment>()

        onView(withId(R.id.editTextEmail))
            .perform(typeText(INVALID_TEST_EMAIL))
        onView(withId(R.id.editTextPassword))
            .perform(typeText(TEST_PASSWORD))
        onView(withId(R.id.buttonSignIn))
            .perform(click())

        onView(withText(SIGN_IN_ERROR)).check(matches(isDisplayed()))

    }

    @Test
    fun navigateSignUp() {
        FakeAuthRepository.isAuthenticated = false

        launch<LoginFragment>(
            graphId = R.navigation.login_navigation
        )

        onView(withId(R.id.textViewSignUp))
            .perform(click())

        val backStack = testNavHostController.backStack
        val currentDestination = backStack.last()

        assertEquals(currentDestination.destination.id, R.id.fragmentRegister)
    }
}