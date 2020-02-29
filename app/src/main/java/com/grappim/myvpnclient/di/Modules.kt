package com.grappim.myvpnclient.di

import com.grappim.myvpnclient.api.MixService
import com.grappim.myvpnclient.api.createOkHttpClient
import com.grappim.myvpnclient.api.createRetrofit
import com.grappim.myvpnclient.core.utils.ConnectivityNetwork
import com.grappim.myvpnclient.core.utils.DhcpUtils
import com.grappim.myvpnclient.core.utils.WifiUtils
import com.grappim.myvpnclient.ui.MainPresenter
import com.grappim.myvpnclient.ui.networkinfo.NetworkInfoPresenter
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module

val mixModule = module {
  single(named("client")) {
    createOkHttpClient()
  }
  single(named("retrofit")) {
    createRetrofit(
      "https://geo.ipify.org/",
      get(named("client"))
    )
  }
  single { MixService(get(named("retrofit"))) }

  factory { MainPresenter() }
  factory { NetworkInfoPresenter() }

  single { ConnectivityNetwork(androidContext()) }
  single { DhcpUtils(androidContext()) }
  single { WifiUtils(androidContext()) }
}