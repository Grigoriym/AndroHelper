package com.grappim.myvpnclient.ui.networkinfo

import com.grappim.myvpnclient.entities.IpEntity
import javax.inject.Inject

class GetIpUseCase @Inject constructor(
    private val networkRepository: NetworkRepository
) {

    suspend operator fun invoke(): IpEntity = networkRepository.getIp()

}