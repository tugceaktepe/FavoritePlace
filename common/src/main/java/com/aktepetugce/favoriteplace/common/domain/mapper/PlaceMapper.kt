package com.aktepetugce.favoriteplace.common.domain.mapper

import com.aktepetugce.favoriteplace.common.domain.decider.EmojiDecider
import com.aktepetugce.favoriteplace.common.domain.model.Place
import com.aktepetugce.favoriteplace.common.extension.orZero
import javax.inject.Inject
import com.aktepetugce.favoriteplace.common.data.model.Place as PlaceDTO

class PlaceMapper @Inject constructor(
    private val emojiDecider: EmojiDecider
) : Mapper<PlaceDTO, Place> {
    override fun mapFrom(from: PlaceDTO): Place {
        return Place(
            id = from.id.orEmpty(),
            name = from.name.orEmpty(),
            description = from.description.orEmpty(),
            feeling = Pair(from.feeling, emojiDecider.decideEmoji(from.feeling)),
            imageUrl = from.imageUrl.orEmpty(),
            longitude = from.longitude.orEmpty().toDoubleOrNull().orZero(),
            latitude = from.latitude.orEmpty().toDoubleOrNull().orZero(),
            instanceId = from.instanceId.orZero()
        )
    }
}
