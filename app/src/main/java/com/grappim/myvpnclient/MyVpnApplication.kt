package com.grappim.myvpnclient

import androidx.multidex.MultiDexApplication
import com.grappim.myvpnclient.di.mixModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

class MyVpnApplication : MultiDexApplication() {
  override fun onCreate() {
    super.onCreate()
    startKoin {
      androidContext(this@MyVpnApplication)
      modules(listOf(mixModule))
    }
    Timber.plant(Timber.DebugTree())
  }
}