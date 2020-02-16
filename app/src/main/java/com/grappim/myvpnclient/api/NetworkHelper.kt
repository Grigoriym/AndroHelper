package com.grappim.myvpnclient.api

import com.grappim.myvpnclient.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import java.util.concurrent.TimeUnit

fun createRetrofit(baseUrl: String, client: OkHttpClient): Retrofit = Retrofit.Builder()
  .baseUrl(baseUrl)
  .client(client)
  .addConverterFactory(GsonConverterFactory.create())
  .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
  .build()

fun createOkHttpClient(vararg interceptors: Interceptor): OkHttpClient {
  val okHttp = OkHttpClient.Builder()
  if (BuildConfig.DEBUG) {
    val logging = HttpLoggingInterceptor(HttpLoggingInterceptor.Logger { message ->
      Timber.tag("API").d(message)
    })
    logging.level = HttpLoggingInterceptor.Level.BODY

    okHttp.addInterceptor(logging)
  }

  for (i in interceptors) {
    okHttp.addInterceptor(i)
  }

  return okHttp
    .connectTimeout(20, TimeUnit.SECONDS)
    .readTimeout(20, TimeUnit.SECONDS)
    .writeTimeout(20, TimeUnit.SECONDS)
    .build()
}