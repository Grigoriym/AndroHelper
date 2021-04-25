package com.grappim.androHelper.data.repository

import com.grappim.androHelper.api.IpifyApi
import com.grappim.androHelper.core.functional.Either
import com.grappim.androHelper.data.remote.BaseRepository
import com.grappim.androHelper.data.remote.model.ip.IpEntityDTO
import com.grappim.androHelper.di.modules.IpifyApiQualifier
import com.grappim.androHelper.domain.repository.NetworkRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkRepositoryImpl @Inject constructor(
    @IpifyApiQualifier private val ipifyApi: IpifyApi
) : NetworkRepository, BaseRepository() {

    override suspend fun getIp(): Either<Throwable, IpEntityDTO> = apiCall {
        ipifyApi.getExternalIp()
    }

}