package com.aktepetugce.favoriteplace.location.domain.usecase

import android.net.Uri
import com.aktepetugce.favoriteplace.common.data.repo.AuthRepository
import com.aktepetugce.favoriteplace.common.data.repo.PlaceRepository
import com.aktepetugce.favoriteplace.common.domain.model.Place
import com.aktepetugce.favoriteplace.common.extension.toResult
import com.aktepetugce.favoriteplace.common.model.Result
import com.aktepetugce.favoriteplace.common.util.StorageUtil
import kotlinx.coroutines.flow.flow
import java.util.UUID
import javax.inject.Inject
import com.aktepetugce.favoriteplace.common.data.model.Place as PlaceDTO

class SavePlaceImage @Inject constructor(
    private val repository: PlaceRepository,
    private val authRepository: AuthRepository
) {
    operator fun invoke(imageUri: Uri, place: Place) = flow<Result<*>> {
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
        if (imageUri.toString() == "null") {
            repository.savePlaceDetail(authRepository.getCurrentUserEmail(), placeDTO).collect {
                emit(it)
            }
        } else {
            val imageSaveResult =
                repository.saveImage(StorageUtil.formatImagePath(imageId), imageUri)
            if (imageSaveResult) {
                placeDTO = placeDTO.copy(imageUrl = repository.downloadImageUrl(imageId))
            }
            repository.savePlaceDetail(authRepository.getCurrentUserEmail(), placeDTO).collect {
                emit(it)
            }
        }
    }.toResult()
}
