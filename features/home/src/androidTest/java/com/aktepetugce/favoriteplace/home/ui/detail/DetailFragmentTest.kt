package com.aktepetugce.favoriteplace.home.ui.detail

import android.os.Bundle
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.aktepetugce.favoriteplace.domain.model.Place
import com.aktepetugce.favoriteplace.testing.ui.BaseFragmentTest
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Test
import org.junit.runner.RunWith
import com.aktepetugce.favoriteplace.home.R


@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class DetailFragmentTest : BaseFragmentTest(){

    private val harryPotterStudio = Place(
        id = "12345",
        name = "Warner Bros. : Harry Potter Studio",
        description = "Making of Harry Potter",
        feeling = Pair(5,R.drawable.ic_baseline_exit_to_app_24),
        imageUrl = "https://s32508.pcdn.co/wp-content/uploads/2019/06/Hogwarts-Castle-Model.jpg",
        latitude = "51.690223125643016".toDouble(),
        longitude = "-0.418723663967239".toDouble(),
        instanceId = 10
    )

    @Test
    fun screenIsReady() {
        val bundle = Bundle()
        bundle.putParcelable("place",harryPotterStudio)
        launch<DetailFragment>(fragmentArgs = bundle)
        Espresso.onView(ViewMatchers.withId(R.id.imageViewPlace))
            .check(ViewAssertions.matches(ViewMatchers.withContentDescription(R.string.favorite_location_image)))
        Espresso.onView(ViewMatchers.withId(R.id.textViewName))
            .check(ViewAssertions.matches(isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.textViewDescription))
            .check(ViewAssertions.matches(isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.mapDetail))
            .check(ViewAssertions.matches(isDisplayed()))
    }

}