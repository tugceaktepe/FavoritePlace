package com.aktepetugce.favoriteplace.location.ui.addlocation

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.aktepetugce.favoriteplace.location.R
import com.aktepetugce.favoriteplace.testing.ui.BaseFragmentTest
import com.aktepetugce.favoriteplace.testing.util.constant.PlaceConstants
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.test.assertEquals


@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class AddLocationFragmentTest : BaseFragmentTest() {

    @Test
    fun screenIsReady() {
        launch<AddLocationFragment>()
        Espresso.onView(ViewMatchers.withId(R.id.editTextName))
            .check(ViewAssertions.matches(isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.editTextDescription))
            .check(ViewAssertions.matches(isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.editTextFeeling))
            .check(ViewAssertions.matches(isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.imageViewPhotoPicker))
            .check(ViewAssertions.matches(isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.buttonNext))
            .check(ViewAssertions.matches(isDisplayed()))
    }

    @Test
    fun navigateMaps() {
        launch<AddLocationFragment>(
            graphId = R.navigation.location_navigation
        )

        Espresso.onView(ViewMatchers.withId(R.id.editTextName))
            .perform(ViewActions.typeText(PlaceConstants.LOCATION_NAME))
        Espresso.onView(ViewMatchers.withId(R.id.editTextDescription))
            .perform(ViewActions.typeText(PlaceConstants.DESCRIPTION))
        Espresso.onView(ViewMatchers.withId(R.id.editTextFeeling))
            .perform(ViewActions.typeText(PlaceConstants.FEELING.toString()))
        Espresso.onView(ViewMatchers.withId(R.id.buttonNext))
            .perform(click())

        val backStack = testNavHostController.backStack
        val currentDestination = backStack.last()

        assertEquals(currentDestination.destination.id, R.id.fragmentMaps)
    }

    @Test
    fun enterEmptyLocationName() {
        launch<AddLocationFragment>()

        Espresso.onView(ViewMatchers.withId(R.id.buttonNext))
            .perform(click())

        Espresso.onView(ViewMatchers.withText(R.string.name_empty_error))
            .check(ViewAssertions.matches(isDisplayed()))

    }
}