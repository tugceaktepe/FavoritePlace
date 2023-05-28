package com.aktepetugce.favoriteplace.login.ui.register

import com.aktepetugce.favoriteplace.domain.usecase.authentication.SignUpUseCase
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

class RegisterViewModelTest {
    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    private val authRepository = FakeAuthRepository()
    private val signUpUseCase = SignUpUseCase(authRepository)

    private lateinit var sut: RegisterViewModel

    @Before
    fun setup() {
        sut = RegisterViewModel(
            signUpUseCase = signUpUseCase,
        )
    }

    @Test
    fun stateIsInitial() = runTest {
        assertEquals(
            null,
            sut.uiState.value,
        )
    }

    @Test
    fun stateIsUserRegisteredWhenRegistrationCompleted() = runTest {
        val collectJob =
            launch(UnconfinedTestDispatcher()) { sut.uiState.collect() }

        sut.signUp(TEST_EMAIL, TEST_PASSWORD)
        advanceUntilIdle()

        assertEquals(
            RegisterUiState.UserRegistered,
            sut.uiState.value,
        )

        collectJob.cancel()
    }

    @Test
    fun stateIsErrorWhenRegistrationFailure() = runTest {
        val collectJob =
            launch(UnconfinedTestDispatcher()) { sut.uiState.collect() }

        sut.signUp(INVALID_TEST_EMAIL, TEST_PASSWORD)
        advanceUntilIdle()

        assertIs<RegisterUiState.Error>(sut.uiState.value)

        collectJob.cancel()
    }

    @Test
    fun whenUserAndPasswordIsValidThenReturnTrue() {
        assertTrue(
            sut.isUserNamePasswordValid(
                TEST_EMAIL,
                TEST_PASSWORD
            )
        )
    }
}
