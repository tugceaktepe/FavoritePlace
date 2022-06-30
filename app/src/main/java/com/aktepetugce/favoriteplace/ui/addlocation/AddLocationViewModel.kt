package com.aktepetugce.favoriteplace.ui.addlocation

import com.aktepetugce.favoriteplace.base.BaseViewModel
import com.aktepetugce.favoriteplace.domain.usecase.authentication.AuthUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AddLocationViewModel @Inject constructor(
    private val authUseCases: AuthUseCases
) : BaseViewModel() {

    val currentUserEmail get() = authUseCases.currentUserEmail.invoke()
}