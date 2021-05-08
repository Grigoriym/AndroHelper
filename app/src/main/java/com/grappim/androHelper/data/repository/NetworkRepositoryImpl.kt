package com.grappim.androHelper.data.repository

import com.grappim.androHelper.api.IpifyApi
import com.grappim.androHelper.core.executor.CoroutineContextProvider
import com.grappim.androHelper.core.functional.Either
import com.grappim.androHelper.core.functional.map
import com.grappim.androHelper.data.remote.BaseRepository
import com.grappim.androHelper.data.remote.model.ip.IpInfoMapper.toDomain
import com.grappim.androHelper.di.modules.IpifyApiQualifier
import com.grappim.androHelper.domain.ip.GetIpUseCase
import com.grappim.androHelper.domain.repository.NetworkRepository
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkRepositoryImpl @Inject constructor(
    @IpifyApiQualifier private val ipifyApi: IpifyApi,
    private val coroutineContextProvider: CoroutineContextProvider
) : NetworkRepository, BaseRepository() {

    override suspend fun getIp(): Either<Throwable, GetIpUseCase.IpInfo> =
        apiCall {
            ipifyApi.getExternalIp()
        }.map {
            withContext(coroutineContextProvider.io) {
                it.toDomain()
            }
        }

}