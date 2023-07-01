package com.aktepetugce.favoriteplace.location.ui.addlocation

import com.aktepetugce.favoriteplace.testing.util.CoroutineTestRule
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals

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
        val uri = "TEST"
        val collectJob =
            launch(UnconfinedTestDispatcher()) { sut.uiState.collect() }

        sut.saveSelectedPhoto(uri)
        advanceUntilIdle()

        assertEquals(
            uri,
            sut.uiState.value.selectedPhotoUri,
        )
        assertEquals(
            uri,
            sut.getSelectedImageUri(),
        )

        collectJob.cancel()
    }
}
