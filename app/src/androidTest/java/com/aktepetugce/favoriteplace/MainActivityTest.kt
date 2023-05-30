package com.aktepetugce.favoriteplace

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withResourceName
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.activityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.aktepetugce.favoriteplace.core.util.TestIdlingResource
import com.aktepetugce.favoriteplace.testing.repository.FakeAuthRepository
import com.aktepetugce.favoriteplace.testing.util.constant.LoginConstants
import com.aktepetugce.favoriteplace.testing.util.constant.LoginConstants.LOG_OUT_BUTTON_RESOURCE_NAME
import com.aktepetugce.favoriteplace.testing.util.constant.LoginConstants.SCREEN_NAVIGATION_TIME
import com.aktepetugce.favoriteplace.ui.MainActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class MainActivityTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @get:Rule
    val activityScenarioRule = activityScenarioRule<MainActivity>()

    @Before
    fun setUp(){
        IdlingRegistry.getInstance().register(TestIdlingResource.countingIdlingResource)
    }

    @Test
    fun signOut() {
        FakeAuthRepository.isAuthenticated = true
        Thread.sleep(SCREEN_NAVIGATION_TIME)
        onView(withResourceName(LOG_OUT_BUTTON_RESOURCE_NAME)).perform(click())
        onView(withResourceName("textViewSignIn")).check(matches(isDisplayed()));
    }

    @Test
    fun signOutWithError() {
        FakeAuthRepository.isAuthenticated = true
        Thread.sleep(SCREEN_NAVIGATION_TIME)
        FakeAuthRepository.isAuthenticated = false
        onView(withResourceName(LOG_OUT_BUTTON_RESOURCE_NAME)).perform(click())
        onView(withText(LoginConstants.SIGN_OUT_ERROR)).check(
            matches(
                isDisplayed()
            )
        )
    }

    @After
    fun onClose(){
        IdlingRegistry.getInstance().unregister(TestIdlingResource.countingIdlingResource)
    }
}