package com.aktepetugce.favoriteplace.common.domain.model

import androidx.annotation.DrawableRes
import com.aktepetugce.favoriteplace.common.R


enum class Feeling(@DrawableRes val emojiIcon: Int) {
    EMPTY(R.drawable.ic_launcher_background),
    AWFUL(R.drawable.ic_launcher_background),
    BAD(R.drawable.ic_launcher_background),
    OKAY(R.drawable.ic_launcher_background),
    GOOD(R.drawable.ic_launcher_background),
    GREAT(R.drawable.ic_launcher_background)
}