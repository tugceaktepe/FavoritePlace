package com.aktepetugce.favoriteplace.di

import android.content.Context
import com.aktepetugce.favoriteplace.R
import com.aktepetugce.favoriteplace.data.repo.AuthRepository
import com.aktepetugce.favoriteplace.data.repo.AuthRepositoryImpl
import com.aktepetugce.favoriteplace.data.repo.PlaceRepository
import com.aktepetugce.favoriteplace.data.repo.PlaceRepositoryImpl
import com.aktepetugce.favoriteplace.domain.mapper.PlaceMapper
import com.aktepetugce.favoriteplace.domain.usecase.authentication.*
import com.aktepetugce.favoriteplace.domain.usecase.place.*
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideAuthRepository(
        auth: FirebaseAuth
    ): AuthRepository = AuthRepositoryImpl(auth)

    @Provides
    @Singleton
    fun providePlaceRepository(
        database: FirebaseDatabase,
        databaseReference: DatabaseReference,
        storage: FirebaseStorage,
        storageReference : StorageReference
    ): PlaceRepository = PlaceRepositoryImpl(database, databaseReference,storage, storageReference)


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
        fetchPlaces = FetchPlaces(placeMapper,placeRepository),
        downloadImageUrl = DownloadImageUrl(placeRepository)
    )

    @Provides
    @Singleton
    fun providePlaceMapper() = PlaceMapper()

    @Singleton
    @Provides
    fun provideGlide(@ApplicationContext context: Context) = Glide
        .with(context).setDefaultRequestOptions(
            RequestOptions().placeholder(R.drawable.ic_launcher_foreground)
                .error(R.drawable.ic_launcher_foreground)
        )
}