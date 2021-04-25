package com.grappim.androHelper.core.utils

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.provider.Settings
import android.telephony.TelephonyManager
import androidx.core.content.ContextCompat

object GeneralUtils {

  fun checkReadPhoneStatePermission(context: Context): Boolean =
    ContextCompat.checkSelfPermission(
      context,
      Manifest.permission.READ_PHONE_STATE
    ) == PackageManager.PERMISSION_GRANTED

  @SuppressLint("HardwareIds")
  @Throws(SecurityException::class)
  fun getImei(context: Context): String {
    val tm = context.getSystemService(Context.TELEPHONY_SERVICE) as? TelephonyManager
    var imei: String?
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
      imei = "On Android 10 >= I can't get the IMEI"
    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      imei = tm?.imei
      if (imei == null) {
        imei = Settings.Secure.getString(
          context.contentResolver,
          Settings.Secure.ANDROID_ID
        )
      }
    } else {
      imei = if (tm?.deviceId != null && tm.deviceId != "000000000000000") {
        tm.deviceId
      } else {
        Settings.Secure.getString(
          context.contentResolver,
          Settings.Secure.ANDROID_ID
        )
      }
    }
    return imei ?: ""
  }

}