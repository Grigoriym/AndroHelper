package com.grappim.myvpnclient.ui.networkinfo

import com.grappim.myvpnclient.core.functional.Either
import com.grappim.myvpnclient.entities.IpEntityDTO
import javax.inject.Inject

class GetIpUseCase @Inject constructor(
    private val networkRepository: NetworkRepository
) {

    suspend operator fun invoke(): Either<Throwable, IpEntityDTO> = networkRepository.getIp()

}