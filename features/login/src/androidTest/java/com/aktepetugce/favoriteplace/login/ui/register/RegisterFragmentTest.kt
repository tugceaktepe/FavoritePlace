package com.aktepetugce.favoriteplace.login.ui.register

import android.net.Uri
import androidx.navigation.navOptions
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.aktepetugce.favoriteplace.login.R
import com.aktepetugce.favoriteplace.testing.util.constant.LoginConstants
import com.aktepetugce.favoriteplace.testing.ui.BaseFragmentTest
import com.aktepetugce.favoriteplace.testing.util.constant.LoginConstants.INVALID_TEST_EMAIL
import com.aktepetugce.favoriteplace.testing.util.constant.LoginConstants.SIGN_UP_ERROR
import com.aktepetugce.favoriteplace.testing.util.constant.LoginConstants.TEST_EMAIL
import com.aktepetugce.favoriteplace.testing.util.constant.LoginConstants.TEST_PASSWORD
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.every
import io.mockk.verify
import org.junit.Test
import org.junit.runner.RunWith

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
        every {
            navController.navigate(Uri.parse(LoginConstants.HOME_URI),
                navOptions {
                    popUpTo(R.id.fragmentRegister) { inclusive = true }
                })
        }returns Unit

        launch<RegisterFragment>()

        Espresso.onView(withId(R.id.editTextEmail))
            .perform(ViewActions.typeText(TEST_EMAIL))
        Espresso.onView(withId(R.id.editTextPassword))
            .perform(ViewActions.typeText(TEST_PASSWORD))
        Espresso.onView(withId(R.id.buttonSignUp))
            .perform(ViewActions.click())

        verify {
            navController.navigate(Uri.parse(LoginConstants.HOME_URI),
            navOptions {
                popUpTo(R.id.fragmentRegister) { inclusive = true }
            })
        }
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
        every {
            navController.navigate(R.id.action_fragmentRegister_to_fragmentLogin)
        } returns Unit

        launch<RegisterFragment>()

        Espresso.onView(withId(R.id.textViewLogin))
            .perform(ViewActions.click())

        verify {
            navController.navigate(R.id.action_fragmentRegister_to_fragmentLogin)
        }
    }
}