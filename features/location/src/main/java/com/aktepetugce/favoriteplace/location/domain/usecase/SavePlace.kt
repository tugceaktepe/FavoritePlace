package com.aktepetugce.favoriteplace.location.domain.usecase

import android.net.Uri
import com.aktepetugce.favoriteplace.common.data.repo.AuthRepository
import com.aktepetugce.favoriteplace.common.data.repo.PlaceRepository
import com.aktepetugce.favoriteplace.common.domain.model.Place
import com.aktepetugce.favoriteplace.common.extension.toResult
import com.aktepetugce.favoriteplace.common.model.Result
import com.aktepetugce.favoriteplace.common.util.StorageUtil
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.UUID
import javax.inject.Inject
import com.aktepetugce.favoriteplace.common.data.model.Place as PlaceDTO

class SavePlace @Inject constructor(
    private val repository: PlaceRepository,
    private val authRepository: AuthRepository,
) {
    operator fun invoke(imageUri: Uri, place: Place): Flow<Result<Unit>> {
        val imageId = place.id.ifEmpty { UUID.randomUUID().toString() }
        var placeDTO = PlaceDTO(
            id = imageId,
            name = place.name,
            description = place.description,
            feeling = place.feeling.first,
            imageUrl = place.imageUrl ?: "",
            longitude = place.longitude.toString(),
            latitude = place.latitude.toString(),
            instanceId = if (place.instanceId == 0L) System.currentTimeMillis() else place.instanceId
        )
        return flow {
            if (imageUri.toString() == "null") {
                emit(repository.savePlaceDetail(authRepository.getCurrentUserEmail(), placeDTO))
            } else {
                val imagePath = StorageUtil.formatImagePath(imageId)
                val uploadTaskResult = repository.saveImage(imagePath, imageUri)
                if (uploadTaskResult) {
                    val imageUrl = repository.downloadImageUrl(imagePath)
                    placeDTO = placeDTO.copy(imageUrl = imageUrl)
                    emit(repository.savePlaceDetail(authRepository.getCurrentUserEmail(), placeDTO))
                }
            }
        }.toResult()
    }
}
