package com.grappim.androHelper.domain.ip

import com.grappim.androHelper.core.functional.Either
import com.grappim.androHelper.data.remote.model.ip.IpEntityDTO
import com.grappim.androHelper.domain.repository.NetworkRepository
import javax.inject.Inject

class GetIpUseCase @Inject constructor(
    private val networkRepository: NetworkRepository
) {

    suspend operator fun invoke(): Either<Throwable, IpEntityDTO> = networkRepository.getIp()

}