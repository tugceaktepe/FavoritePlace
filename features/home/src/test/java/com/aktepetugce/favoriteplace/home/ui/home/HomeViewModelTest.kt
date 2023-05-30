package com.aktepetugce.favoriteplace.home.ui.home

import com.aktepetugce.favoriteplace.domain.decider.EmojiDecider
import com.aktepetugce.favoriteplace.domain.mapper.PlaceMapper
import com.aktepetugce.favoriteplace.domain.usecase.authentication.SignOutUseCase
import com.aktepetugce.favoriteplace.domain.usecase.location.FetchPlacesUseCase
import com.aktepetugce.favoriteplace.testing.data.places
import com.aktepetugce.favoriteplace.testing.repository.FakeAuthRepository
import com.aktepetugce.favoriteplace.testing.repository.FakePlaceRepository
import com.aktepetugce.favoriteplace.testing.util.CoroutineTestRule
import com.aktepetugce.favoriteplace.testing.util.constant.LoginConstants.SIGN_OUT_ERROR
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
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
        FakeAuthRepository.isAuthenticated = true

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
        assertFalse((viewModel.uiState.value as HomeUiState.PlaceListLoaded).isEmpty())

        collectJob.cancel()
    }

    @Test
    fun stateIsErrorWhenFetchPlacesFailure() = runTest {
        FakeAuthRepository.isAuthenticated = false

        val collectJob =
            launch(UnconfinedTestDispatcher()) { viewModel.uiState.collect() }

        viewModel.fetchPlaces(true)
        advanceUntilIdle()

        assertIs<HomeUiState.Error>(viewModel.uiState.value)

        collectJob.cancel()
    }

    @Test
    fun stateIsUserSignedOutWhenUserSignedOut() = runTest {
        FakeAuthRepository.isAuthenticated = true

        val collectJob =
            launch(UnconfinedTestDispatcher()) { viewModel.uiState.collect() }

        viewModel.signOut()
        advanceUntilIdle()

        assertEquals(
            HomeUiState.UserSignedOut,
            viewModel.uiState.value,
        )

        collectJob.cancel()
    }

    @Test
    fun stateIsErrorWhenSignOutFailure() = runTest {
        FakeAuthRepository.isAuthenticated = false

        val collectJob =
            launch(UnconfinedTestDispatcher()) { viewModel.uiState.collect() }

        viewModel.signOut()
        advanceUntilIdle()

        assertEquals(
            HomeUiState.Error(message = SIGN_OUT_ERROR, isNotShown = true),
            viewModel.uiState.value,
        )

        collectJob.cancel()
    }

    @Test
    fun stateIsErrorMessageShown() = runTest {
        val collectJob =
            launch(UnconfinedTestDispatcher()) { viewModel.uiState.collect() }

        viewModel.errorMessageShown()

        assertEquals(
            HomeUiState.Error(message = "", isNotShown = false),
            viewModel.uiState.value,
        )

        collectJob.cancel()
    }
}
