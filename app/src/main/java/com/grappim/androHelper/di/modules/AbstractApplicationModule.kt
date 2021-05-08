package com.grappim.androHelper.di.modules

import com.grappim.androHelper.core.executor.CoroutineContextProvider
import com.grappim.androHelper.core.executor.CoroutineContextProviderImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class AbstractApplicationModule {

    @Binds
    abstract fun provideCoroutineContextProvider(
        coroutineContextProviderImpl: CoroutineContextProviderImpl
    ): CoroutineContextProvider

}