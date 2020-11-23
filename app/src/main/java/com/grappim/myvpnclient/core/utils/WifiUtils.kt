package com.grappim.myvpnclient.core.utils

import android.Manifest
import android.net.wifi.SupplicantState
import android.net.wifi.WifiInfo
import android.net.wifi.WifiManager
import android.os.Build
import androidx.annotation.RequiresPermission
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WifiUtils @Inject constructor(
    private val wifiManager: WifiManager
) {

    @RequiresPermission(Manifest.permission.ACCESS_FINE_LOCATION)
    fun getSsid(): String? {
        var ssid = "<unknown ssid>"
        val wifiInfo = wifiManager.connectionInfo
        if (wifiInfo?.supplicantState == SupplicantState.COMPLETED) {
            ssid = wifiInfo.ssid
        }
        return ssid
    }

    @RequiresPermission(Manifest.permission.ACCESS_FINE_LOCATION)
    fun getBssid(): String {
        var bSsid = "<unknown bssid>"
        if (wifiManager.connectionInfo?.supplicantState == SupplicantState.COMPLETED) {
            bSsid = wifiManager.connectionInfo.bssid
        }
        return bSsid
    }

    fun getLinkSpeed(): String = "${wifiManager.connectionInfo?.linkSpeed ?: N_A}"

    fun getNetworkId(): String = "${wifiManager.connectionInfo?.networkId ?: N_A}"

    fun getFrequency(): String {
        return if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            wifiManager.connectionInfo?.frequency?.let {
                "${wifiManager.connectionInfo.frequency} ${WifiInfo.FREQUENCY_UNITS}"
            } ?: let {
                N_A
            }
        } else {
            N_A
        }
    }

}