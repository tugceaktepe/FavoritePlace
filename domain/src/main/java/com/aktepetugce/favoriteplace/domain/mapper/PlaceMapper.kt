package com.aktepetugce.favoriteplace.domain.mapper
import com.aktepetugce.favoriteplace.core.extension.orZero
import com.aktepetugce.favoriteplace.data.annotation.DefaultDispatcher
import com.aktepetugce.favoriteplace.domain.decider.EmojiDecider
import com.aktepetugce.favoriteplace.domain.model.Place
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject
import com.aktepetugce.favoriteplace.data.model.Place as PlaceDTO

class PlaceMapper @Inject constructor(
    private val emojiDecider: EmojiDecider,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher
) : Mapper<List<PlaceDTO>?, List<Place>> {
    override suspend fun mapFrom(from: List<PlaceDTO>?): List<Place> {
        return withContext(defaultDispatcher) {
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
