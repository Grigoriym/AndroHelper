package com.grappim.myvpnclient.utils

import android.Manifest
import android.content.Context
import android.net.wifi.SupplicantState
import android.net.wifi.WifiInfo
import android.os.Build
import androidx.annotation.RequiresPermission
import com.grappim.myvpnclient.core.extensions.getWifiManager

class WifiUtils internal constructor(private val context: Context) {

  @RequiresPermission(Manifest.permission.ACCESS_FINE_LOCATION)
  fun getSsid(): String? {
    var ssid = "<unknown ssid>"
    val wifiInfo = context.getWifiManager()?.connectionInfo
    if (wifiInfo?.supplicantState == SupplicantState.COMPLETED) {
      ssid = wifiInfo.ssid
    }
    return ssid
  }

  @RequiresPermission(Manifest.permission.ACCESS_FINE_LOCATION)
  fun getBssid(): String {
    var bSsid = "<unknown bssid>"
    val wifiInfo = context.getWifiManager()?.connectionInfo
    if (wifiInfo?.supplicantState == SupplicantState.COMPLETED) {
      bSsid = wifiInfo.bssid
    }
    return bSsid
  }

  fun getLinkSpeed(): String {
    val wifiInfo = context.getWifiManager()?.connectionInfo
    return "${wifiInfo?.linkSpeed ?: "N/A"}"
  }

  fun getFrequency(): String {
    return if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
      val info = context.getWifiManager()?.connectionInfo
      info?.frequency?.let {
        "${info.frequency} ${WifiInfo.FREQUENCY_UNITS}"
      } ?: let {
        "N/A"
      }
    } else {
      "N/A"
    }
  }

}