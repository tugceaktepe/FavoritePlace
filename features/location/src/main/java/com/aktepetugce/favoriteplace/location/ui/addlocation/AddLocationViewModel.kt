package com.aktepetugce.favoriteplace.location.ui.addlocation

import androidx.lifecycle.ViewModel
import com.aktepetugce.favoriteplace.location.domain.GetCurrentUserEmail
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AddLocationViewModel @Inject constructor(
    private val currentUserUseCase: GetCurrentUserEmail
) : ViewModel() {

    val currentUserEmail get() = currentUserUseCase.invoke()
}
