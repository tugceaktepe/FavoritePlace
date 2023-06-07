package com.aktepetugce.favoriteplace.location.ui.map

import android.os.Bundle
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.longClick
import androidx.test.espresso.matcher.ViewMatchers.withContentDescription
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.aktepetugce.favoriteplace.location.R
import com.aktepetugce.favoriteplace.testing.ui.BaseFragmentTest
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class MapsFragmentTest : BaseFragmentTest(){

    @Test
    fun screenIsReady(){
        val bundle = Bundle()
        bundle.putInt("homeDestinationId", R.id.fragmentAddLocation)
        launch<MapsFragment>(bundle)
        onView(withContentDescription(R.string.map_description)).perform(longClick())
        //TEST TOOLBAR ON MAIN ACTIVITY
    }
}