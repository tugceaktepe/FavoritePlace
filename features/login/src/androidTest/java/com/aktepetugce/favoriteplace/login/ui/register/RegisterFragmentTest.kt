package com.aktepetugce.favoriteplace.login.ui.register

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.aktepetugce.favoriteplace.login.R
import com.aktepetugce.favoriteplace.testing.ui.BaseFragmentTest
import com.aktepetugce.favoriteplace.testing.util.constant.LoginConstants.INVALID_TEST_EMAIL
import com.aktepetugce.favoriteplace.testing.util.constant.LoginConstants.SIGN_UP_ERROR
import com.aktepetugce.favoriteplace.testing.util.constant.LoginConstants.TEST_EMAIL
import com.aktepetugce.favoriteplace.testing.util.constant.LoginConstants.TEST_PASSWORD
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.test.assertEquals

@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class RegisterFragmentTest : BaseFragmentTest(){

    @Test
    fun screenIsReady() {
        launch<RegisterFragment>()
        Espresso.onView(withId(R.id.textViewWelcome))
            .check(ViewAssertions.matches(ViewMatchers.withText(R.string.welcome_text)))
        Espresso.onView(withId(R.id.textViewSignUp))
            .check(ViewAssertions.matches(ViewMatchers.withText(R.string.sign_up)))
        Espresso.onView(withId(R.id.editTextEmail))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(withId(R.id.editTextPassword))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(withId(R.id.buttonSignUp))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(withId(R.id.textViewLogin))
            .check(ViewAssertions.matches(ViewMatchers.withText(R.string.sign_in_text)))
    }

    @Test
    fun signUpSuccessfully()  {

        launch<RegisterFragment>()

        Espresso.onView(withId(R.id.editTextEmail))
            .perform(ViewActions.typeText(TEST_EMAIL))
        Espresso.onView(withId(R.id.editTextPassword))
            .perform(ViewActions.typeText(TEST_PASSWORD))
        Espresso.onView(withId(R.id.buttonSignUp))
            .perform(ViewActions.click())

        //TODO: add assertion after solving navigation problem about deeplinks :
        //https://stackoverflow.com/questions/76326962/android-navigation-destination-that-matches-request-navdeeplinkrequest-uri-a
    }

    @Test
    fun signUpWithError()  {

        launch<RegisterFragment>()

        Espresso.onView(withId(R.id.editTextEmail))
            .perform(ViewActions.typeText(INVALID_TEST_EMAIL))
        Espresso.onView(withId(R.id.editTextPassword))
            .perform(ViewActions.typeText(TEST_PASSWORD))
        Espresso.onView(withId(R.id.buttonSignUp))
            .perform(ViewActions.click())

        Espresso.onView(ViewMatchers.withText(SIGN_UP_ERROR))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

    }

    @Test
    fun navigateSignIn()  {

        launch<RegisterFragment>(
            graphId = R.navigation.login_navigation,
            currentDestination = R.id.fragmentRegister
        )

        Espresso.onView(withId(R.id.textViewLogin))
            .perform(ViewActions.click())

        val backStack = testNavHostController.backStack
        val currentDestination = backStack.last()

        assertEquals(currentDestination.destination.id, R.id.fragmentLogin)
    }
}