package com.aktepetugce.favoriteplace.common.domain.mapper

import com.aktepetugce.favoriteplace.common.domain.decider.EmojiDecider
import com.aktepetugce.favoriteplace.common.domain.model.Place
import com.aktepetugce.favoriteplace.common.extension.orZero
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import com.aktepetugce.favoriteplace.common.data.model.Place as PlaceDTO

class PlaceMapper @Inject constructor(
    private val emojiDecider: EmojiDecider
) : Mapper<List<PlaceDTO>?, List<Place>> {
    override suspend fun mapFrom(from: List<PlaceDTO>?): List<Place> {
        return withContext(Dispatchers.Default) {
            from?.sortedBy { it.instanceId }?.map {
                Place(
                    id = it.id.orEmpty(),
                    name = it.name.orEmpty(),
                    description = it.description.orEmpty(),
                    feeling = Pair(it.feeling, emojiDecider.decideEmoji(it.feeling)),
                    imageUrl = it.imageUrl.orEmpty(),
                    longitude = it.longitude.orEmpty().toDoubleOrNull().orZero(),
                    latitude = it.latitude.orEmpty().toDoubleOrNull().orZero(),
                    instanceId = it.instanceId.orZero()
                )
            }.orEmpty()
        }
    }
}
