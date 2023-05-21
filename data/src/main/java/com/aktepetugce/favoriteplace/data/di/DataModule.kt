package com.aktepetugce.favoriteplace.data.di

import com.aktepetugce.favoriteplace.data.repository.AuthRepository
import com.aktepetugce.favoriteplace.data.repository.AuthRepositoryImpl
import com.aktepetugce.favoriteplace.data.repository.PlaceRepository
import com.aktepetugce.favoriteplace.data.repository.PlaceRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {

    @Binds
    fun bindsAuthRepository(
        authRepositoryImpl: AuthRepositoryImpl,
    ): AuthRepository

    @Binds
    fun bindsPlaceRepository(
        newsRepository: PlaceRepositoryImpl,
    ): PlaceRepository
}
