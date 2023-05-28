package com.aktepetugce.favoriteplace.home.ui.home

import com.aktepetugce.favoriteplace.domain.decider.EmojiDecider
import com.aktepetugce.favoriteplace.domain.mapper.PlaceMapper
import com.aktepetugce.favoriteplace.domain.usecase.authentication.SignOutUseCase
import com.aktepetugce.favoriteplace.domain.usecase.location.FetchPlacesUseCase
import com.aktepetugce.favoriteplace.testing.data.places
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
import kotlin.test.assertEquals
import kotlin.test.assertIs

class HomeViewModelTest {
    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    private val placeRepository = FakePlaceRepository()
    private val authRepository = FakeAuthRepository()
    private val mapper = PlaceMapper(
        emojiDecider = EmojiDecider(),
        defaultDispatcher = UnconfinedTestDispatcher()
    )
    private val fetchPlacesUseCase = FetchPlacesUseCase(
        placeRepository = placeRepository,
        authRepository = authRepository,
        placeMapper = mapper
    )
    private val signOutUseCase = SignOutUseCase(authRepository)

    private lateinit var viewModel: HomeViewModel

    @Before
    fun setup() {
        viewModel = HomeViewModel(
            fetchPlacesUseCase,
            signOutUseCase
        )
    }

    @Test
    fun stateIsPlaceListLoadedWhenFetchCompleted() = runTest {
        val collectJob =
            launch(UnconfinedTestDispatcher()) { viewModel.uiState.collect() }

        viewModel.fetchPlaces(true)
        advanceUntilIdle()

        assertEquals(
            HomeUiState.PlaceListLoaded(
                mapper.mapFrom(places)
            ),
            viewModel.uiState.value
        )

        collectJob.cancel()
    }

    @Test
    fun stateIsErrorWhenSignInFailure() = runTest {
        FakeAuthRepository.isAuthenticated = false

        val collectJob =
            launch(UnconfinedTestDispatcher()) { viewModel.uiState.collect() }

        viewModel.fetchPlaces(true)
        advanceUntilIdle()

        assertIs<HomeUiState.Error>(viewModel.uiState.value)

        collectJob.cancel()
    }

    /*@Test
    fun stateIsUserSignedInWhenUserIsNotSignedIn() = runTest {
        val collectJob =
            launch(UnconfinedTestDispatcher()) { viewModel.uiState.collect() }

        viewModel.checkUser()
        advanceUntilIdle()

        assertEquals(
            LoginUiState.UserNotSignedIn,
            viewModel.uiState.value,
        )

        collectJob.cancel()
    }*/
}
