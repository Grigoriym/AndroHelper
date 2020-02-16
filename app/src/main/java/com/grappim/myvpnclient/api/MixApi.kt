package com.grappim.myvpnclient.api

import com.grappim.myvpnclient.entities.IpEntity
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface MixApi {

  @GET("/")
  fun getExternalIp(
    @Query("format") format: String = "json"
  ) : Single<IpEntity>

}