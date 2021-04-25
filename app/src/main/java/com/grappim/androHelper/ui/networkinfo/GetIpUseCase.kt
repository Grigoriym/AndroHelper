package com.grappim.androHelper.ui.networkinfo

import com.grappim.androHelper.core.functional.Either
import com.grappim.androHelper.entities.IpEntityDTO
import javax.inject.Inject

class GetIpUseCase @Inject constructor(
    private val networkRepository: NetworkRepository
) {

    suspend operator fun invoke(): Either<Throwable, IpEntityDTO> = networkRepository.getIp()

}