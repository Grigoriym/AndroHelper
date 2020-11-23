package com.grappim.myvpnclient.api

import com.grappim.myvpnclient.BuildConfig
import com.grappim.myvpnclient.entities.IpEntity
import retrofit2.http.GET
import retrofit2.http.Query

interface IpifyApi {

    @GET("api/v1")
    suspend fun getExternalIp(
        @Query("apiKey") apiKey: String = BuildConfig.IpifyApiKey,
        @Query("format") format: String = "json"
    ): IpEntity

}