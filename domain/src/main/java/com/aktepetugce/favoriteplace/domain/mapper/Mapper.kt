package com.aktepetugce.favoriteplace.common.domain.mapper

interface Mapper<F, T> {
    suspend fun mapFrom(from: F): T {
        TODO("Your default implementation here")
    }

    fun mapTo(to: T): F {
        TODO("Your default implementation here")
    }
}
