package com.grappim.androHelper.domain.repository

import com.grappim.androHelper.core.functional.Either
import com.grappim.androHelper.domain.ip.GetIpUseCase

interface NetworkRepository {

    suspend fun getIp(): Either<Throwable, GetIpUseCase.IpInfo>
}