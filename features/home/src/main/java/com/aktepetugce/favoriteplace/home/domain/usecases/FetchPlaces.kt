package com.aktepetugce.favoriteplace.home.domain.usecases

import com.aktepetugce.favoriteplace.common.data.model.Place
import com.aktepetugce.favoriteplace.common.data.repo.PlaceRepository
import com.aktepetugce.favoriteplace.common.model.Response
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class FetchPlaces @Inject constructor(
    private val repository: PlaceRepository,
) {
    operator fun invoke(userEmail: String) = flow {
        repository.fetchPlaces(userEmail).collect { response ->
            when (response) {
                is Response.Success<*> -> {
                    val placeList = response.data as List<Place>
                    emit(Response.Success(placeList))
                }
                else -> {
                    emit(response)
                }
            }
        }
    }
}
