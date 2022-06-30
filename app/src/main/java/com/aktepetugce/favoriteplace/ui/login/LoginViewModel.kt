package com.aktepetugce.favoriteplace.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.aktepetugce.favoriteplace.base.BaseViewModel
import com.aktepetugce.favoriteplace.domain.usecase.authentication.AuthUseCases
import com.aktepetugce.favoriteplace.util.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authUseCases: AuthUseCases
) : BaseViewModel() {

    val isUserAuthenticated get() = authUseCases.isUserAuthenticated()

    private val _isSignInSuccess = MutableLiveData<Boolean>()
    val isSignInSuccess: LiveData<Boolean> = _isSignInSuccess

    fun signIn(userEmail: String, password: String) = viewModelScope.launch {
        authUseCases.signIn.invoke(userEmail, password).collect { response ->
            when (response) {
                is Response.Success<*> -> {
                    _isLoading.value= false
                    _isSignInSuccess.value = true
                }
                is Response.Error -> {
                    _isLoading.value= false
                    _error.value = response.message
                }
                else -> _isLoading.value = true
            }
        }
    }

    private val _isSignUpSuccess = MutableLiveData<Boolean>()
    val isSignUpSuccess: LiveData<Boolean> = _isSignUpSuccess

    fun signUp(userEmail: String, password: String) = viewModelScope.launch {
        authUseCases.signUp.invoke(userEmail, password).collect { response ->
            when (response) {
                is Response.Success<*> -> {
                    _isLoading.value= false
                    _isSignUpSuccess.value = true
                }
                is Response.Error -> {
                    _isLoading.value= false
                    _error.value = response.message
                }
                else -> _isLoading.value = true
            }
        }
    }

}