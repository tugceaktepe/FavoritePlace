package com.aktepetugce.favoriteplace.location.ui.map

import android.net.Uri
import com.aktepetugce.favoriteplace.domain.model.Place
import com.aktepetugce.favoriteplace.domain.usecase.location.SavePlaceUseCase
import com.aktepetugce.favoriteplace.location.R
import com.aktepetugce.favoriteplace.testing.repository.FakeAuthRepository
import com.aktepetugce.favoriteplace.testing.repository.FakePlaceRepository
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
import kotlin.test.assertIs

// TODO: used because of Uri. But remove Uri from viewModel later
@RunWith(RobolectricTestRunner::class)
class MapsViewModelTest {
    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    private lateinit var sut: MapsViewModel

    private val placeRepository = FakePlaceRepository()
    private val authRepository = FakeAuthRepository()
    private val savePlaceUseCase = SavePlaceUseCase(
        placeRepository,
        authRepository
    )

    private val harryPotterStudio = Place(
        id = "12345",
        name = "Warner Bros. : Harry Potter Studio",
        description = "Making of Harry Potter",
        feeling = Pair(5, R.drawable.ic_done_icon),
        imageUrl = "https://s32508.pcdn.co/wp-content/uploads/2019/06/Hogwarts-Castle-Model.jpg",
        latitude = "51.690223125643016".toDouble(),
        longitude = "-0.418723663967239".toDouble(),
        instanceId = 10
    )

    @Before
    fun setup() {
        sut = MapsViewModel(
            savePlaceUseCase
        )
    }

    @Test
    fun stateIsLocationIsAdded_WhenSaveCompleted() = runTest {
        FakeAuthRepository.isAuthenticated = true

        val collectJob =
            launch(UnconfinedTestDispatcher()) { sut.uiState.collect() }

        sut.savePlace(harryPotterStudio, Uri.parse("TEST"))
        advanceUntilIdle()

        assertEquals(
            MapsUiState.LocationIsAdded,
            sut.uiState.value,
        )

        collectJob.cancel()
    }

    @Test
    fun stateIsError_WhenSaveImageIsFailure() = runTest {
        FakeAuthRepository.isAuthenticated = true

        val collectJob =
            launch(UnconfinedTestDispatcher()) { sut.uiState.collect() }

        sut.savePlace(harryPotterStudio, Uri.parse(""))
        advanceUntilIdle()

        assertIs<MapsUiState.Error>(sut.uiState.value)

        collectJob.cancel()
    }

    @Test
    fun stateIsError_WhenDownloadImageUrlIsFailure() = runTest {
        FakeAuthRepository.isAuthenticated = true

        val collectJob =
            launch(UnconfinedTestDispatcher()) { sut.uiState.collect() }

        sut.savePlace(harryPotterStudio.copy(id = "123456"), Uri.parse("TEST"))
        advanceUntilIdle()

        assertIs<MapsUiState.Error>(sut.uiState.value)

        collectJob.cancel()
    }

    @Test
    fun stateIsError_WhenSavePlaceDetailIsFailure() = runTest {
        FakeAuthRepository.isAuthenticated = false
        val collectJob =
            launch(UnconfinedTestDispatcher()) { sut.uiState.collect() }

        sut.savePlace(harryPotterStudio, Uri.parse("TEST"))
        advanceUntilIdle()

        assertIs<MapsUiState.Error>(sut.uiState.value)

        collectJob.cancel()
    }
}
