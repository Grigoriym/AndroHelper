package com.grappim.myvpnclient.api

import com.grappim.myvpnclient.BuildConfig
import com.grappim.myvpnclient.entities.IpEntity
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface MixApi {

  @GET("api/v1")
  fun getExternalIp(
    @Query("apiKey") apiKey: String = BuildConfig.IpifyApiKey,
    @Query("format") format: String = "json"
  ): Single<IpEntity>

}