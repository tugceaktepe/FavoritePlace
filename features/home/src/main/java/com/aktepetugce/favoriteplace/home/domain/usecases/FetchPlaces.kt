package com.aktepetugce.favoriteplace.home.domain.usecases

import com.aktepetugce.favoriteplace.common.data.repo.PlaceRepository
import com.aktepetugce.favoriteplace.common.domain.mapper.PlaceMapper
import com.aktepetugce.favoriteplace.common.model.Result
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import com.aktepetugce.favoriteplace.common.data.model.Place as PlaceDTO

class FetchPlaces @Inject constructor(
    private val repository: PlaceRepository,
    private val placeMapper: PlaceMapper
) {
    @Suppress("UNCHECKED_CAST")
    operator fun invoke(userEmail: String) = flow {
        repository.fetchPlaces(userEmail).collect { response ->
            when (response) {
                is com.aktepetugce.favoriteplace.common.model.Resource.Result.Success<*> -> {
                    //TODO: refactor unchecked cast warning
                    val placeList = response.data as ArrayList<PlaceDTO>
                    val placeListForUI = placeList.sortedBy { it.instanceId }.map {
                        placeMapper.mapFrom(it)
                    }
                    emit(Result.Success(placeListForUI))
                }

                else -> {
                    emit(response)
                }
            }
        }
    }
}
