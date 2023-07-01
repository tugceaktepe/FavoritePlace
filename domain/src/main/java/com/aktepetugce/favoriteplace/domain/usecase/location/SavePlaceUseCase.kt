package com.aktepetugce.favoriteplace.domain.usecase.location

import com.aktepetugce.favoriteplace.core.util.StorageUtil
import com.aktepetugce.favoriteplace.data.repository.AuthRepository
import com.aktepetugce.favoriteplace.data.repository.PlaceRepository
import com.aktepetugce.favoriteplace.domain.model.Place
import com.aktepetugce.favoriteplace.domain.model.Result
import com.aktepetugce.favoriteplace.domain.model.toResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.UUID
import javax.inject.Inject
import com.aktepetugce.favoriteplace.data.model.Place as PlaceDTO

class SavePlaceUseCase @Inject constructor(
    private val repository: PlaceRepository,
    private val authRepository: AuthRepository,
) {
    operator fun invoke(byteArray: ByteArray, place: Place): Flow<Result<Unit>> {
        val imageId = place.id.ifEmpty { UUID.randomUUID().toString() }
        var placeDTO = PlaceDTO(
            id = imageId,
            name = place.name,
            description = place.description,
            feeling = place.feeling.first,
            imageUrl = place.imageUrl,
            longitude = place.longitude.toString(),
            latitude = place.latitude.toString(),
            instanceId = if (place.instanceId == 0L) System.currentTimeMillis() else place.instanceId
        )
        return flow {
            if (byteArray.isEmpty()) {
                emit(repository.savePlaceDetail(authRepository.getCurrentUserEmail(), placeDTO))
            } else {
                val imagePath = StorageUtil.formatImagePath(imageId)
                val uploadTaskResult = repository.saveImage(imagePath, byteArray)
                if (uploadTaskResult) {
                    val imageUrl = repository.downloadImageUrl(imagePath)
                    placeDTO = placeDTO.copy(imageUrl = imageUrl)
                    emit(repository.savePlaceDetail(authRepository.getCurrentUserEmail(), placeDTO))
                }
            }
        }.toResult()
    }
}
