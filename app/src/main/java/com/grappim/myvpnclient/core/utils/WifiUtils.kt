package com.grappim.myvpnclient.core.utils

import android.Manifest
import android.content.Context
import android.net.wifi.SupplicantState
import android.net.wifi.WifiInfo
import android.os.Build
import androidx.annotation.RequiresPermission
import com.grappim.myvpnclient.core.extensions.getWifiManager

class WifiUtils internal constructor(private val context: Context) {

  private val connectionInfo = context.getWifiManager()?.connectionInfo

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
    if (connectionInfo?.supplicantState == SupplicantState.COMPLETED) {
      bSsid = connectionInfo.bssid
    }
    return bSsid
  }

  fun getLinkSpeed(): String = "${connectionInfo?.linkSpeed ?: N_A}"

  fun getNetworkId(): String = "${connectionInfo?.networkId ?: N_A}"

  fun getFrequency(): String {
    return if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
      connectionInfo?.frequency?.let {
        "${connectionInfo.frequency} ${WifiInfo.FREQUENCY_UNITS}"
      } ?: let {
        N_A
      }
    } else {
      N_A
    }
  }

}