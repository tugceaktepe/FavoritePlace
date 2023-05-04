package com.aktepetugce.favoriteplace.di

import com.aktepetugce.favoriteplace.common.annotation.IoDispatcher
import com.aktepetugce.favoriteplace.data.repo.AuthRepository
import com.aktepetugce.favoriteplace.data.repo.AuthRepositoryImpl
import com.aktepetugce.favoriteplace.data.repo.PlaceRepository
import com.aktepetugce.favoriteplace.data.repo.PlaceRepositoryImpl
import com.aktepetugce.favoriteplace.domain.mapper.PlaceMapper
import com.aktepetugce.favoriteplace.domain.usecase.authentication.AuthUseCases
import com.aktepetugce.favoriteplace.domain.usecase.authentication.GetCurrentUserEmail
import com.aktepetugce.favoriteplace.domain.usecase.authentication.IsUserAuthenticated
import com.aktepetugce.favoriteplace.domain.usecase.authentication.SignIn
import com.aktepetugce.favoriteplace.domain.usecase.authentication.SignOut
import com.aktepetugce.favoriteplace.domain.usecase.authentication.SignUp
import com.aktepetugce.favoriteplace.domain.usecase.place.DownloadImageUrl
import com.aktepetugce.favoriteplace.domain.usecase.place.FetchPlaces
import com.aktepetugce.favoriteplace.domain.usecase.place.PlaceUseCases
import com.aktepetugce.favoriteplace.domain.usecase.place.SavePlaceDetail
import com.aktepetugce.favoriteplace.domain.usecase.place.SavePlaceImage
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
    ): AuthRepository = AuthRepositoryImpl(auth, dispatcher)

    @Provides
    @Singleton
    fun providePlaceRepository(
        database: FirebaseDatabase,
        databaseReference: DatabaseReference,
        storage: FirebaseStorage,
        storageReference: StorageReference,
        @IoDispatcher dispatcher: CoroutineDispatcher
    ): PlaceRepository = PlaceRepositoryImpl(database, databaseReference, storage, storageReference, dispatcher)

    @Provides
    @Singleton
    fun provideAuthUseCases(
        authRepository: AuthRepository
    ) = AuthUseCases(
        isUserAuthenticated = IsUserAuthenticated(authRepository),
        signOut = SignOut(authRepository),
        signUp = SignUp(authRepository),
        signIn = SignIn(authRepository),
        currentUserEmail = GetCurrentUserEmail(authRepository)
    )

    @Provides
    @Singleton
    fun providePlaceUseCases(
        placeRepository: PlaceRepository,
        placeMapper: PlaceMapper
    ) = PlaceUseCases(
        savePlaceDetail = SavePlaceDetail(placeRepository),
        savePlaceImage = SavePlaceImage(placeRepository),
        fetchPlaces = FetchPlaces(placeMapper, placeRepository),
        downloadImageUrl = DownloadImageUrl(placeRepository)
    )

    @Provides
    @Singleton
    fun providePlaceMapper() = PlaceMapper()

    @IoDispatcher
    @Provides
    fun providesIoDispatcher(): CoroutineDispatcher = Dispatchers.IO
}
