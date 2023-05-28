package com.aktepetugce.favoriteplace.login.ui.login

import com.aktepetugce.favoriteplace.domain.usecase.authentication.CheckUserAuthenticatedUseCase
import com.aktepetugce.favoriteplace.domain.usecase.authentication.SignInUseCase
import com.aktepetugce.favoriteplace.testing.repository.FakeAuthRepository
import com.aktepetugce.favoriteplace.testing.util.CoroutineTestRule
import com.aktepetugce.favoriteplace.testing.util.constant.LoginConstants.INVALID_TEST_EMAIL
import com.aktepetugce.favoriteplace.testing.util.constant.LoginConstants.TEST_EMAIL
import com.aktepetugce.favoriteplace.testing.util.constant.LoginConstants.TEST_PASSWORD
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
import kotlin.test.assertTrue

class LoginViewModelTest {
    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    private val authRepository = FakeAuthRepository()
    private val signInUseCase = SignInUseCase(authRepository)
    private val checkUserAuthenticatedUseCase = CheckUserAuthenticatedUseCase(authRepository)

    private lateinit var sut: LoginViewModel

    @Before
    fun setup() {
        sut = LoginViewModel(
            signInUseCase = signInUseCase,
            checkUserAuthenticatedUseCase = checkUserAuthenticatedUseCase
        )
    }

    @Test
    fun stateIsUserSignedInWhenSignInCompleted() = runTest {
        val collectJob =
            launch(UnconfinedTestDispatcher()) { sut.uiState.collect() }

        sut.signIn(TEST_EMAIL, TEST_PASSWORD)
        advanceUntilIdle()

        assertEquals(
            LoginUiState.UserSignedIn,
            sut.uiState.value,
        )

        collectJob.cancel()
    }

    @Test
    fun stateIsErrorWhenSignInFailure() = runTest {
        val collectJob =
            launch(UnconfinedTestDispatcher()) { sut.uiState.collect() }

        sut.signIn(INVALID_TEST_EMAIL, TEST_PASSWORD)
        advanceUntilIdle()

        assertIs<LoginUiState.Error>(sut.uiState.value)

        collectJob.cancel()
    }

    @Test
    fun stateIsUserSignedInWhenUserIsNotSignedIn() = runTest {
        val collectJob =
            launch(UnconfinedTestDispatcher()) { sut.uiState.collect() }

        sut.checkUser()
        advanceUntilIdle()

        assertEquals(
            LoginUiState.UserNotSignedIn,
            sut.uiState.value,
        )

        collectJob.cancel()
    }

    @Test
    fun whenUserAndPasswordIsValidThenReturnTrue() {
        assertTrue(sut.isUserNamePasswordValid(TEST_EMAIL, TEST_PASSWORD))
    }
}
