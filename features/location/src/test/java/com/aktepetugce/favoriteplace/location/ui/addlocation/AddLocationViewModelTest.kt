package com.aktepetugce.favoriteplace.location.ui.addlocation

import android.net.Uri
import com.aktepetugce.favoriteplace.testing.util.CoroutineTestRule
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import kotlin.test.assertEquals

// TODO: used because of Uri. But remove Uri from viewModel later
@RunWith(RobolectricTestRunner::class)
class AddLocationViewModelTest {
    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    private lateinit var sut: AddLocationViewModel

    @Before
    fun setup() {
        sut = AddLocationViewModel()
    }

    @Test
    fun updateSelectedPhotoUriToState_WhenSelectedPhotoSaved() = runTest {
        val uri = Uri.parse("TEST")
        val collectJob =
            launch(UnconfinedTestDispatcher()) { sut.uiState.collect() }

        sut.saveSelectedPhoto(uri)
        advanceUntilIdle()

        assertEquals(
            uri,
            sut.uiState.value.selectedPhotoUri,
        )
        assertEquals(
            uri.toString(),
            sut.getSelectedImageUri(),
        )

        collectJob.cancel()
    }
}
