package com.grappim.myvpnclient.api

import com.grappim.myvpnclient.entities.IpEntity
import io.reactivex.Single
import retrofit2.Retrofit

class MixService(
  retrofit: Retrofit
) : MixApi {

  private val mixApi by lazy { retrofit.create(MixApi::class.java) }

  override fun getExternalIp(format: String): Single<IpEntity> =
    mixApi.getExternalIp()

}