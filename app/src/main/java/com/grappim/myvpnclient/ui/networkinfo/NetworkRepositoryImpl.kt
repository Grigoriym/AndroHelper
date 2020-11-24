package com.grappim.myvpnclient.ui.networkinfo

import com.grappim.myvpnclient.api.IpifyApi
import com.grappim.myvpnclient.core.functional.Either
import com.grappim.myvpnclient.di.qualifiers.IpifyApiQualifier
import com.grappim.myvpnclient.entities.IpEntityDTO
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkRepositoryImpl @Inject constructor(
    @IpifyApiQualifier private val ipifyApi: IpifyApi
) : NetworkRepository {

    override suspend fun getIp(): Either<Throwable, IpEntityDTO> = try {
        Either.Right(ipifyApi.getExternalIp())
    } catch (throwable: Throwable) {
        Either.Left(throwable)
    }

}