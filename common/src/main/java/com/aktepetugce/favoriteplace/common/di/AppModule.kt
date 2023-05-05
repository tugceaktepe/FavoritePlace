package com.aktepetugce.favoriteplace.common.di

import com.aktepetugce.favoriteplace.common.annotation.IoDispatcher
import com.aktepetugce.favoriteplace.common.data.repo.AuthRepository
import com.aktepetugce.favoriteplace.common.data.repo.AuthRepositoryImpl
import com.aktepetugce.favoriteplace.common.data.repo.PlaceRepository
import com.aktepetugce.favoriteplace.common.data.repo.PlaceRepositoryImpl
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideAuthRepository(
        auth: FirebaseAuth,
        @IoDispatcher dispatcher: CoroutineDispatcher
    ): AuthRepository =
        AuthRepositoryImpl(auth, dispatcher)

    @Provides
    @Singleton
    fun providePlaceRepository(
        database: FirebaseDatabase,
        databaseReference: DatabaseReference,
        storage: FirebaseStorage,
        storageReference: StorageReference,
        @IoDispatcher dispatcher: CoroutineDispatcher
    ): PlaceRepository =
        PlaceRepositoryImpl(
            database,
            databaseReference,
            storage,
            storageReference,
            dispatcher
        )

    @IoDispatcher
    @Provides
    fun providesIoDispatcher(): CoroutineDispatcher = Dispatchers.IO
}
