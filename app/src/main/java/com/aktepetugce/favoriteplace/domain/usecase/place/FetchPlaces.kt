package com.aktepetugce.favoriteplace.domain.usecase.place

import com.aktepetugce.favoriteplace.data.model.Place
import com.aktepetugce.favoriteplace.data.repo.PlaceRepository
import com.aktepetugce.favoriteplace.domain.mapper.PlaceMapper
import com.aktepetugce.favoriteplace.domain.uimodel.UIPlace
import com.aktepetugce.favoriteplace.util.Response
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class FetchPlaces (
    private val placeMapper: PlaceMapper,
    private val repository: PlaceRepository,
) {
    operator fun invoke(userEmail : String) = flow {
        repository.fetchPlaces(userEmail).collect { response ->
            when(response){
                is Response.Success<*> -> {
                    val placeList = response.data as List<Place>
                    val uiPlaceList = mutableListOf<UIPlace>()
                    placeList.sortedBy { it.instanceId }.map {
                        val uiPlace = placeMapper.map(it)
                        if (uiPlace != null) {
                            uiPlaceList.add(uiPlace)
                        }
                    }
                    emit(Response.Success(uiPlaceList))
                }
                else -> {
                    emit(response)
                }
            }

        }

    }
}