package com.aktepetugce.favoriteplace.common.domain.decider

import com.aktepetugce.favoriteplace.common.domain.model.Feeling
import javax.inject.Inject

class EmojiDecider @Inject constructor() {
    fun decideEmoji(feelingRate: Int): Int{
        return when (feelingRate) {
            EMPTY -> Feeling.EMPTY.emojiIcon
            AWFUL -> Feeling.AWFUL.emojiIcon
            BAD -> Feeling.BAD.emojiIcon
            OKAY -> Feeling.OKAY.emojiIcon
            GOOD -> Feeling.GOOD.emojiIcon
            GREAT -> Feeling.GREAT.emojiIcon
            else -> Feeling.EMPTY.emojiIcon
        }
    }

    companion object {
        const val EMPTY = 0
        const val AWFUL = 1
        const val BAD = 2
        const val OKAY = 3
        const val GOOD = 4
        const val GREAT = 5
    }
}