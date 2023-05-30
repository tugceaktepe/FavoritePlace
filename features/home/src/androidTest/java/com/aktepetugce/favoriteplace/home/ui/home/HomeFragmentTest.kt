package com.aktepetugce.favoriteplace.home.ui.home

import android.net.Uri
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.aktepetugce.favoriteplace.home.R
import com.aktepetugce.favoriteplace.home.ui.home.adapter.PlaceRecyclerAdapter
import com.aktepetugce.favoriteplace.testing.repository.FakeAuthRepository
import com.aktepetugce.favoriteplace.testing.ui.BaseFragmentTest
import com.aktepetugce.favoriteplace.testing.util.constant.LoginConstants
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.verify

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
        var adapter : PlaceRecyclerAdapter? = null
        launch<HomeFragment>{
             adapter = this.requireView()
                .findViewById<RecyclerView>(R.id.recyclerViewLocations).adapter as PlaceRecyclerAdapter
        }
        onView(withId(R.id.recyclerViewLocations))
            .perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(1, click()))
        verify(navController).navigate(HomeFragmentDirections.actionFragmentHomeToFragmentDetail(
            adapter?.currentList?.get(1)!!
        ))
    }
}