package com.grappim.myvpnclient.ui.networkinfo

import com.grappim.myvpnclient.core.functional.Either
import com.grappim.myvpnclient.entities.IpEntityDTO

interface NetworkRepository {

    suspend fun getIp(): Either<Throwable, IpEntityDTO>
}