package com.aktepetugce.favoriteplace.home.ui.home

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.aktepetugce.favoriteplace.home.R
import com.aktepetugce.favoriteplace.testing.repository.FakeAuthRepository
import com.aktepetugce.favoriteplace.testing.ui.BaseFragmentTest
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.test.assertEquals

@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class HomeFragmentTest : BaseFragmentTest() {

    @Test
    fun screenIsReady() {
        FakeAuthRepository.isAuthenticated = true
        launch<HomeFragment>()
        onView(withId(R.id.recyclerViewLocations))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun navigatePlaceDetail() {
        FakeAuthRepository.isAuthenticated = true
        launch<HomeFragment>(
            graphId = R.navigation.home_navigation
        )
        onView(withId(R.id.recyclerViewLocations))
            .perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(1, click()))
        val backStack = testNavHostController.backStack
        val currentDestination = backStack.last()

        assertEquals(currentDestination.destination.id, R.id.fragmentDetail)
    }
}