package com.aktepetugce.favoriteplace.testing.di

import com.aktepetugce.favoriteplace.data.di.DataModule
import com.aktepetugce.favoriteplace.data.repository.AuthRepository
import com.aktepetugce.favoriteplace.data.repository.PlaceRepository
import com.aktepetugce.favoriteplace.testing.repository.FakeAuthRepository
import com.aktepetugce.favoriteplace.testing.repository.FakePlaceRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [DataModule::class]
)
interface TestDataModule {

    @Binds
    fun bindsFakePlaceRepository(
        fakePlaceRepository: FakePlaceRepository,
    ): PlaceRepository

    @Binds
    fun bindsFakeAuthRepository(
        fakeAuthRepository: FakeAuthRepository,
    ): AuthRepository
}
