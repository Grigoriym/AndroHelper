package com.grappim.androHelper.domain.repository

import com.grappim.androHelper.core.functional.Either
import com.grappim.androHelper.data.remote.model.ip.IpEntityDTO

interface NetworkRepository {

    suspend fun getIp(): Either<Throwable, IpEntityDTO>
}