package com.grappim.androHelper.domain.ip

import com.grappim.androHelper.core.functional.Either
import com.grappim.androHelper.data.remote.model.ip.IpInfoDTO
import com.grappim.androHelper.domain.repository.NetworkRepository
import javax.inject.Inject

class GetIpUseCase @Inject constructor(
    private val networkRepository: NetworkRepository
) {

    suspend operator fun invoke(): Either<Throwable, IpInfo> = networkRepository.getIp()

    data class IpInfo(
        val ip: String,
        val city: String,
        val region: String,
        val country: String,
        val latitude: Double,
        val longitude: Double,
        val isp: String
    )

}