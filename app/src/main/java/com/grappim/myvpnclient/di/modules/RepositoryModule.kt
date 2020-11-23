package com.grappim.myvpnclient.di.modules

import com.grappim.myvpnclient.ui.networkinfo.NetworkRepository
import com.grappim.myvpnclient.ui.networkinfo.NetworkRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent

@InstallIn(ApplicationComponent::class)
@Module
abstract class RepositoryModule {
    @Binds
    abstract fun bindsNetworkRepository(
        networkRepositoryImpl: NetworkRepositoryImpl
    ): NetworkRepository
}