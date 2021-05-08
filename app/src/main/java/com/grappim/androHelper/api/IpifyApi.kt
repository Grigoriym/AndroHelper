package com.grappim.androHelper.api

import com.grappim.androHelper.BuildConfig
import com.grappim.androHelper.data.remote.model.ip.IpInfoDTO
import retrofit2.http.GET
import retrofit2.http.Query

interface IpifyApi {

    @GET("api/v1")
    suspend fun getExternalIp(
        @Query("apiKey") apiKey: String = BuildConfig.IpifyApiKey,
        @Query("format") format: String = "json"
    ): IpInfoDTO

}