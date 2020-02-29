package com.grappim.myvpnclient.core.extensions

import android.content.Context
import android.net.ConnectivityManager
import android.net.DhcpInfo
import android.net.wifi.WifiManager
import android.telephony.TelephonyManager
import androidx.core.content.ContextCompat

fun Context.getWifiManager(): WifiManager? =
  ContextCompat.getSystemService(this, WifiManager::class.java)

fun Context.getConnectivityManager(): ConnectivityManager? =
  ContextCompat.getSystemService(this, ConnectivityManager::class.java)

fun Context.getTelephonyManager(): TelephonyManager? =
  ContextCompat.getSystemService(this, TelephonyManager::class.java)

fun Context.getDhcpInfo(): DhcpInfo? = this.getWifiManager()?.dhcpInfo
