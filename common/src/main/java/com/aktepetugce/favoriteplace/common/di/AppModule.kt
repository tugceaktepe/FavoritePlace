package com.aktepetugce.favoriteplace.common.di

import com.aktepetugce.favoriteplace.common.annotation.IoDispatcher
import com.aktepetugce.favoriteplace.common.data.repo.AuthRepository
import com.aktepetugce.favoriteplace.common.data.repo.AuthRepositoryImpl
import com.aktepetugce.favoriteplace.common.data.repo.PlaceRepository
import com.aktepetugce.favoriteplace.common.data.repo.PlaceRepositoryImpl
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
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
        firestore: FirebaseFirestore,
        storage: FirebaseStorage,
        storageReference: StorageReference,
        @IoDispatcher dispatcher: CoroutineDispatcher
    ): PlaceRepository =
        PlaceRepositoryImpl(
            firestore,
            storage,
            storageReference,
            dispatcher
        )

    @IoDispatcher
    @Provides
    fun providesIoDispatcher(): CoroutineDispatcher = Dispatchers.IO
}
