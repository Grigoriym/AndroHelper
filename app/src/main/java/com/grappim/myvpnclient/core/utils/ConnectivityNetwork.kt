package com.grappim.myvpnclient.core.utils

import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.wifi.WifiManager
import android.os.Build
import android.telephony.TelephonyManager
import android.text.format.Formatter
import java.net.Inet4Address
import java.net.NetworkInterface
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ConnectivityNetwork @Inject constructor(
    private val connectivityManager: ConnectivityManager,
    private val wifiManager: WifiManager
) {

    companion object {
        private const val WIFI = "wifi"
        private const val CELLULAR = "cellular"
        private const val ETHERNET = "ethernet"
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
        val info = connectivityManager.activeNetworkInfo
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
            ip = Formatter.formatIpAddress(wifiManager.connectionInfo?.ipAddress!!)
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

    @Suppress("DEPRECATION")
    private fun getNetworkType(): String {
        var networkType: String = NOT_CONNECTED
        val cm = connectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val an = cm?.activeNetwork
            an?.let {
                val nc = cm.getNetworkCapabilities(it) ?: return NOT_CONNECTED
                networkType = when {
                    nc.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> WIFI
                    nc.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> CELLULAR
                    nc.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> ETHERNET
                    else -> NOT_CONNECTED
                }
            } ?: let {
                return NOT_CONNECTED
            }
        } else {
            val activeNetwork = connectivityManager.activeNetworkInfo
            networkType = if (activeNetwork != null) {
                when (activeNetwork.type) {
                    ConnectivityManager.TYPE_WIFI -> WIFI
                    ConnectivityManager.TYPE_MOBILE -> CELLULAR
                    ConnectivityManager.TYPE_ETHERNET -> ETHERNET
                    else -> NOT_CONNECTED
                }
            } else {
                NOT_CONNECTED
            }
        }
        return networkType
    }

}