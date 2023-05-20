package com.aktepetugce.favoriteplace.domain.model

import androidx.annotation.DrawableRes
import com.aktepetugce.favoriteplace.domain.R

enum class Feeling(@DrawableRes val emojiIcon: Int) {
    EMPTY(R.drawable.ic_emoji),
    AWFUL(R.drawable.ic_emoji),
    BAD(R.drawable.ic_emoji),
    OKAY(R.drawable.ic_emoji),
    GOOD(R.drawable.ic_emoji),
    GREAT(R.drawable.ic_emoji)
}
