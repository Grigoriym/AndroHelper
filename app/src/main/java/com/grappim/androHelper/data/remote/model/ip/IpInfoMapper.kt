package com.grappim.androHelper.data.remote.model.ip

import com.grappim.androHelper.domain.ip.GetIpUseCase

object IpInfoMapper {

    fun IpInfoDTO.toDomain(): GetIpUseCase.IpInfo =
        GetIpUseCase.IpInfo(
            ip = this.ip,
            city = this.location.city,
            region = this.location.region,
            country = this.location.country,
            latitude = this.location.lat,
            longitude = this.location.lng,
            isp = this.isp
        )

}