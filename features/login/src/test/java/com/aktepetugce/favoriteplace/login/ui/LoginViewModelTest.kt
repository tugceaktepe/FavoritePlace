package com.aktepetugce.favoriteplace.login.ui

import com.aktepetugce.favoriteplace.domain.usecase.authentication.CheckUserAuthenticatedUseCase
import com.aktepetugce.favoriteplace.domain.usecase.authentication.SignInUseCase
import com.aktepetugce.favoriteplace.login.ui.login.LoginUiState
import com.aktepetugce.favoriteplace.login.ui.login.LoginViewModel
import com.aktepetugce.favoriteplace.testing.repository.FakeAuthRepository
import com.aktepetugce.favoriteplace.testing.util.CoroutineTestRule
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue

class LoginViewModelTest {
    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    private val authRepository = FakeAuthRepository()
    private val signInUseCase = SignInUseCase(authRepository)
    private val checkUserAuthenticatedUseCase = CheckUserAuthenticatedUseCase(authRepository)

    private lateinit var viewModel: LoginViewModel

    @Before
    fun setup() {
        viewModel = LoginViewModel(
            signInUseCase = signInUseCase,
            checkUserAuthenticatedUseCase = checkUserAuthenticatedUseCase
        )
    }

    @Test
    fun stateIsInitial() = runTest {
        assertEquals(
            LoginUiState.InitalState,
            viewModel.uiState.value,
        )
    }

    @Test
    fun stateIsUserSignedInWhenSignInCompleted() = runTest {
        val collectJob =
            launch(UnconfinedTestDispatcher()) { viewModel.uiState.collect() }

        viewModel.signIn(TEST_EMAIL, TEST_PASSWORD)

        assertEquals(
            LoginUiState.UserSignedIn,
            viewModel.uiState.value,
        )

        collectJob.cancel()
    }

    @Test
    fun stateIsErrorWhenSignInFailure() = runTest {
        val collectJob =
            launch(UnconfinedTestDispatcher()) { viewModel.uiState.collect() }

        viewModel.signIn(INVALID_TEST_EMAIL, TEST_PASSWORD)

        assertIs<LoginUiState.Error>(viewModel.uiState.value)

        collectJob.cancel()
    }

    @Test
    fun stateIsUserSignedInWhenUserIsAlreadyAuthenticated() = runTest {
        val collectJob =
            launch(UnconfinedTestDispatcher()) { viewModel.uiState.collect() }

        viewModel.checkUser()

        assertEquals(
            LoginUiState.UserSignedIn,
            viewModel.uiState.value,
        )

        collectJob.cancel()
    }

    @Test
    fun whenUserAndPasswordIsValidThenReturnTrue() {
        assertTrue(viewModel.isUserNamePasswordValid(TEST_EMAIL, TEST_PASSWORD))
    }

    companion object {
        const val TEST_EMAIL = "test@mail.com"
        const val INVALID_TEST_EMAIL = "testmail.com"
        const val TEST_PASSWORD = "password"
    }
}
