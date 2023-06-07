package com.aktepetugce.favoriteplace.location.ui.addlocation

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavBackStackEntry
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
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class AddLocationFragmentTest : BaseFragmentTest() {

    @MockK
    lateinit var navBackStackEntry: NavBackStackEntry

    @MockK
    lateinit var savedStateHandle: SavedStateHandle

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

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
        every {
            navController.currentBackStackEntry
        } returns navBackStackEntry

        every { navBackStackEntry.savedStateHandle } returns savedStateHandle
        every { savedStateHandle.get<Int>("HOME_DESTINATION_ID") } returns 1
        every { navController.navigate(any<Int>(),any()) } returns Unit

        launch<AddLocationFragment>()

        Espresso.onView(ViewMatchers.withId(R.id.editTextName))
            .perform(ViewActions.typeText(PlaceConstants.LOCATION_NAME))
        Espresso.onView(ViewMatchers.withId(R.id.editTextDescription))
            .perform(ViewActions.typeText(PlaceConstants.DESCRIPTION))
        Espresso.onView(ViewMatchers.withId(R.id.editTextFeeling))
            .perform(ViewActions.typeText(PlaceConstants.FEELING.toString()))
        Espresso.onView(ViewMatchers.withId(R.id.buttonNext))
            .perform(click())

        verify {
            navController.navigate(any<Int>(), any())
        }
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