package com.grappim.androHelper.ui.networkinfo

import com.grappim.androHelper.core.functional.Either
import com.grappim.androHelper.entities.IpEntityDTO

interface NetworkRepository {

    suspend fun getIp(): Either<Throwable, IpEntityDTO>
}