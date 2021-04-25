package com.grappim.androHelper.di.modules

import com.grappim.androHelper.ui.networkinfo.NetworkRepository
import com.grappim.androHelper.ui.networkinfo.NetworkRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
abstract class RepositoryModule {

    @Binds
    abstract fun bindsNetworkRepository(
        networkRepositoryImpl: NetworkRepositoryImpl
    ): NetworkRepository

}