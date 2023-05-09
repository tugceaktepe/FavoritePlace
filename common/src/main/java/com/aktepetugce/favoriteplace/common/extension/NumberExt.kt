package com.aktepetugce.favoriteplace.common.extension

inline fun <reified T : Number> T?.orZero() = this ?: when (T::class) {
    Long::class -> 0L as T
    Double::class -> 0.0 as T
    Float::class -> 0f as T
    Int::class -> 0 as T
    else -> 0 as T
}
