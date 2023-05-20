package com.aktepetugce.favoriteplace.testing.di

import com.aktepetugce.favoriteplace.core.annotation.DefaultDispatcher
import com.aktepetugce.favoriteplace.core.annotation.IoDispatcher
import com.aktepetugce.favoriteplace.core.di.DispatchersModule
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.test.TestDispatcher

// https://developer.android.com/training/dependency-injection/hilt-testing

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [DispatchersModule::class]
)
object TestDispatchersModule {

    @IoDispatcher
    @Provides
    fun providesTestIODispatcher(testDispatcher: TestDispatcher): CoroutineDispatcher = testDispatcher

    @DefaultDispatcher
    @Provides
    fun providesTestDefaultDispatcher(testDispatcher: TestDispatcher): CoroutineDispatcher = testDispatcher
}
