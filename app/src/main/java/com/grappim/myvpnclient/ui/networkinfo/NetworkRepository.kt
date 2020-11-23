package com.grappim.myvpnclient.ui.networkinfo

import com.grappim.myvpnclient.entities.IpEntity

interface NetworkRepository {

    suspend fun getIp(): IpEntity
}