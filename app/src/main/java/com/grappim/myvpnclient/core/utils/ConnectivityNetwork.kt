package com.grappim.myvpnclient.core.utils

import android.content.Context
import android.net.ConnectivityManager
import android.telephony.TelephonyManager
import android.text.format.Formatter
import com.grappim.myvpnclient.core.extensions.getConnectivityManager
import com.grappim.myvpnclient.core.extensions.getWifiManager
import java.net.Inet4Address
import java.net.NetworkInterface

class ConnectivityNetwork internal constructor(private val context: Context) {

  companion object {
    private const val WIFI = "wifi"
    private const val CELLULAR = "cellular"
    private const val NOT_CONNECTED = "not_connected"
  }

  /**
   * https://stackoverflow.com/questions/10831578/how-to-find-mac-address-of-an-android-device-programmatically
   */
  fun getMacAddress(): String =
    try {
      NetworkInterface.getNetworkInterfaces()
        .toList()
        .find { it.name.equals(WLAN_0, true) }
        ?.hardwareAddress
        ?.joinToString(separator = ":") { byte -> "%02X".format(byte) }
        ?: DEFAULT_MAC_ADDRESS
    } catch (e: Exception) {
      e.printStackTrace()
      DEFAULT_MAC_ADDRESS
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

  private fun getMobileInternalIpAddress(): String? {
    val en = NetworkInterface.getNetworkInterfaces()
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
    return DEFAULT_IP_ADDRESS
  }

  private fun getWifiInternalIpAddress(): String? {
    var ip = ""
    try {
      val wm = context.getWifiManager()
      ip = Formatter.formatIpAddress(wm?.connectionInfo?.ipAddress!!)
    } catch (e: Exception) {

    }
    if (ip.isEmpty()) {
      try {
        val en = NetworkInterface.getNetworkInterfaces()
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
      } catch (e: Exception) {

      }
    }

    if (ip.isEmpty())
      ip = "127.0.0.1"
    return ip
  }

  private fun getNetworkType(): String {
    var networkType: String = NOT_CONNECTED
    val activeNetwork = context.getConnectivityManager()?.activeNetworkInfo
    if (activeNetwork != null) {
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