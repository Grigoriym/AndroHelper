package com.grappim.androHelper.ui.networkinfo

import com.grappim.androHelper.api.IpifyApi
import com.grappim.androHelper.core.functional.Either
import com.grappim.androHelper.di.qualifiers.IpifyApiQualifier
import com.grappim.androHelper.entities.IpEntityDTO
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