package com.aktepetugce.favoriteplace.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aktepetugce.favoriteplace.domain.usecase.authentication.AuthUseCases
import com.aktepetugce.favoriteplace.util.Response
import com.aktepetugce.favoriteplace.util.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authUseCases: AuthUseCases
) : ViewModel() {

    val error = SingleLiveEvent<String>()
    val isLoading = SingleLiveEvent<Boolean>()
    val isSignInSuccess = SingleLiveEvent<Boolean>()
    val isSignUpSuccess = SingleLiveEvent<Boolean>()

    val isUserAuthenticated get() = authUseCases.isUserAuthenticated()
    
    fun signIn(userEmail: String, password: String) = viewModelScope.launch {
        authUseCases.signIn(userEmail, password).collect { response ->
            when (response) {
                is Response.Success<*> -> {
                    isLoading.value= false
                    isSignInSuccess.value = true
                }
                is Response.Error -> {
                    isLoading.value= false
                    error.value = response.message
                }
                else -> isLoading.value = true
            }
        }
    }

    fun signUp(userEmail: String, password: String) = viewModelScope.launch {
        authUseCases.signUp(userEmail, password).collect { response ->
            when (response) {
                is Response.Success<*> -> {
                    isLoading.value= false
                    isSignUpSuccess.value = true
                }
                is Response.Error -> {
                    isLoading.value= false
                    error.value = response.message
                }
                else -> isLoading.value = true
            }
        }
    }

}