package com.aktepetugce.favoriteplace.location.domain.usecase

import com.aktepetugce.favoriteplace.common.data.repo.AuthRepository
import com.aktepetugce.favoriteplace.common.data.repo.PlaceRepository
import com.aktepetugce.favoriteplace.common.domain.model.Place
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import com.aktepetugce.favoriteplace.common.data.model.Place as PlaceDTO

class SavePlaceDetail @Inject constructor(
    private val repository: PlaceRepository,
    private val authRepository: AuthRepository
    ) {
    operator fun invoke(placeId: String, url:String?, place: Place) : Flow<Any>{
        val placeDTO = PlaceDTO(
            id = placeId,
            name = place.name,
            description = place.description,
            feeling = place.feeling.first,
            imageUrl = url ?: "",
            longitude = place.longitude.toString(),
            latitude = place.latitude.toString(),
            instanceId = if (place.instanceId == 0L) System.currentTimeMillis() else place.instanceId
        )
        return repository.savePlaceDetail(
            authRepository.getCurrentUserEmail(), placeDTO)
    }

}
