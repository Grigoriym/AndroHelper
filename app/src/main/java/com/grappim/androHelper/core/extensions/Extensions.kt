package com.grappim.androHelper.core.extensions

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.view.View
import com.grappim.androHelper.core.utils.SafeClickListener

fun View.setSafeOnClickListener(onSafeClick: (View) -> Unit) {
    val safeClickListener = SafeClickListener {
        onSafeClick(it)
    }
    setOnClickListener(safeClickListener)
}

fun Context.doOnInternet(
  isConnected: () -> Unit,
  isNotConnected: () -> Unit
) {
    if (this.isConnected()) {
        isConnected()
    } else {
        isNotConnected()
    }
}

/**
 * https://stackoverflow.com/questions/53532406/activenetworkinfo-type-is-deprecated-in-api-level-28
 */
@Suppress("DEPRECATION")
fun Context.isConnected(): Boolean {
    var result = false
    val connectivityManager = getConnectivityManager()
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val networkCapabilities = connectivityManager?.activeNetwork ?: return false
        val actNw = connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
        result = when {
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    } else {
        connectivityManager.run {
            connectivityManager?.activeNetworkInfo?.run {
                result = when (type) {
                  ConnectivityManager.TYPE_WIFI -> true
                  ConnectivityManager.TYPE_MOBILE -> true
                  ConnectivityManager.TYPE_ETHERNET -> true
                    else -> false
                }
            }
        }
    }
    return result
}