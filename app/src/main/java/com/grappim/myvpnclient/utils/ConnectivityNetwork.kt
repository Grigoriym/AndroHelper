package com.grappim.myvpnclient.utils

import android.Manifest
import android.content.Context
import android.net.ConnectivityManager
import android.net.wifi.SupplicantState
import android.telephony.TelephonyManager
import android.text.format.Formatter
import androidx.annotation.RequiresPermission
import com.grappim.myvpnclient.core.extensions.getConnectivityManager
import com.grappim.myvpnclient.core.extensions.getWifiManager
import java.net.Inet4Address
import java.net.NetworkInterface.getNetworkInterfaces
import java.util.*

class ConnectivityNetwork internal constructor(private val context: Context) {

  companion object {
    private const val WIFI = "wifi"
    private const val CELLULAR = "cellular"
    private const val NOT_CONNECTED = "not_connected"
  }

  fun getInternalIpAddress(): String? {
    return when (getNetworkType()) {
      WIFI -> {
        getWifiInternalIpAddress()
      }
      CELLULAR -> {
        getMobileInternalIpAddress()
      }
      else -> {
        NOT_CONNECTED
      }
    }
  }

  fun getMacAddress(): String {
    try {
      val all = Collections.list(getNetworkInterfaces())
      for (nif in all) {
        if (!nif.name.equals("wlan0", ignoreCase = true)) continue
        val macBytes = nif.hardwareAddress ?: return ""
        val res1 = StringBuilder()
        for (b in macBytes) {
          res1.append(String.format("%02X:", b))
        }
        if (res1.isNotEmpty()) {
          res1.deleteCharAt(res1.length - 1)
        }
        return res1.toString()
      }
    } catch (ex: Exception) {
    }
    return "02:00:00:00:00:00"
  }

  @Deprecated("Refactor it")
  fun getNetworkClass(): String? {
    val info = context.getConnectivityManager()?.activeNetworkInfo
    if (info == null || !info.isConnected) return "-" // not connected
    if (info.type == ConnectivityManager.TYPE_WIFI) return "WIFI"
    if (info.type == ConnectivityManager.TYPE_MOBILE) {
      return when (info.subtype) {
        TelephonyManager.NETWORK_TYPE_GPRS,
        TelephonyManager.NETWORK_TYPE_EDGE,
        TelephonyManager.NETWORK_TYPE_CDMA,
        TelephonyManager.NETWORK_TYPE_1xRTT,
        TelephonyManager.NETWORK_TYPE_IDEN,
        TelephonyManager.NETWORK_TYPE_GSM -> {
          "2G"
        }
        TelephonyManager.NETWORK_TYPE_UMTS,
        TelephonyManager.NETWORK_TYPE_EVDO_0,
        TelephonyManager.NETWORK_TYPE_EVDO_A,
        TelephonyManager.NETWORK_TYPE_HSDPA,
        TelephonyManager.NETWORK_TYPE_HSUPA,
        TelephonyManager.NETWORK_TYPE_HSPA,
        TelephonyManager.NETWORK_TYPE_EVDO_B,
        TelephonyManager.NETWORK_TYPE_EHRPD,
        TelephonyManager.NETWORK_TYPE_HSPAP,
        TelephonyManager.NETWORK_TYPE_TD_SCDMA -> {
          "3G"
        }
        TelephonyManager.NETWORK_TYPE_LTE,
        TelephonyManager.NETWORK_TYPE_IWLAN, 19 -> {
          "4G"
        }
        TelephonyManager.NETWORK_TYPE_NR -> {
          "5G"
        }
        else -> "?"
      }
    }
    return "?"
  }

  @RequiresPermission(Manifest.permission.ACCESS_FINE_LOCATION)
  fun getSsid(): String? {
    var ssid = ""
    val wifiManager = context.getWifiManager()
    val wifiInfo = wifiManager?.connectionInfo
    if (wifiInfo?.supplicantState == SupplicantState.COMPLETED) {
      ssid = wifiInfo.ssid
    }
    return ssid
  }

  @RequiresPermission(Manifest.permission.ACCESS_FINE_LOCATION)
  fun getBssid(): String {
    var bSsid = ""
    val wifiManager = context.getWifiManager()
    val wifiInfo = wifiManager?.connectionInfo
    if (wifiInfo?.supplicantState == SupplicantState.COMPLETED) {
      bSsid = wifiInfo.bssid
    }
    return bSsid
  }


  private fun getMobileInternalIpAddress(): String? {
    val en = getNetworkInterfaces()
    while (en.hasMoreElements()) {
      val intf = en.nextElement()
      val enumIpAddr = intf.inetAddresses
      while (enumIpAddr.hasMoreElements()) {
        val inetAddress = enumIpAddr.nextElement()
        if (!inetAddress.isLoopbackAddress && inetAddress is Inet4Address) {
          return inetAddress.getHostAddress()
        }
      }
    }
    return null
  }

  private fun getWifiInternalIpAddress(): String? {
    var ip = ""
    try {
      val wm = context.getWifiManager()
      ip = Formatter.formatIpAddress(wm?.connectionInfo?.ipAddress!!)
    } catch (e: java.lang.Exception) {

    }
    if (ip.isEmpty()) {
      try {
        val en = getNetworkInterfaces()
        while (en.hasMoreElements()) {
          val networkInterface = en.nextElement()
          val enumIpAddr = networkInterface.inetAddresses
          while (enumIpAddr.hasMoreElements()) {
            val inetAddress = enumIpAddr.nextElement()
            if (!inetAddress.isLoopbackAddress && inetAddress is Inet4Address) {
              val host = inetAddress.getHostAddress()
              if (host.isNotEmpty()) {
                ip = host
                break
              }
            }
          }

        }
      } catch (e: java.lang.Exception) {

      }
    }

    if (ip.isEmpty())
      ip = "127.0.0.1"
    return ip
  }

  private fun getNetworkType(): String? {
    var networkType: String? = null
    val connectivityManager = context.getConnectivityManager()
    val activeNetwork = connectivityManager?.activeNetworkInfo
    if (activeNetwork != null) { // connected to the internet
      if (activeNetwork.type == ConnectivityManager.TYPE_WIFI) {
        networkType = WIFI
      } else if (activeNetwork.type == ConnectivityManager.TYPE_MOBILE) {
        networkType = CELLULAR
      }
    } else {
      networkType = NOT_CONNECTED
    }
    return networkType
  }

}