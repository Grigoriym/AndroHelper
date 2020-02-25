package com.grappim.myvpnclient.utils

import android.app.NotificationManager
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkInfo
import android.os.Build
import android.view.View
import androidx.core.content.ContextCompat

fun View.setSafeOnClickListener(onSafeClick: (View) -> Unit) {
  val safeClickListener = SafeClickListener {
    onSafeClick(it)
  }
  setOnClickListener(safeClickListener)
}

fun Context.getNotificationManager() =
  ContextCompat.getSystemService(
    this,
    NotificationManager::class.java
  ) as NotificationManager

val Context.networkInfo: NetworkInfo?
  get() =
    this.connectivityManager?.activeNetworkInfo

inline val Context.connectivityManager: ConnectivityManager?
  get() =
    getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager

fun Context.doOnInternet(
  isConnected: () -> Unit,
  isNotConnected: () -> Unit
) {
  if (this.isConnectedNew()) {
    isConnected()
  } else {
    isNotConnected()
  }
}

fun Context.isConnectedNew(): Boolean {
  var result = false
  val cm = ContextCompat.getSystemService(this, ConnectivityManager::class.java)
  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
    cm?.run {
      cm.getNetworkCapabilities(cm.activeNetwork)?.run {
        result = when {
          hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
          hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
          hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
          else -> false
        }
      }
    }
  } else {
    cm?.run {
      cm.activeNetworkInfo?.run {
        if (type == ConnectivityManager.TYPE_WIFI) {
          result = true
        } else if (type == ConnectivityManager.TYPE_MOBILE) {
          result = true
        }
      }
    }
  }
  return result
}