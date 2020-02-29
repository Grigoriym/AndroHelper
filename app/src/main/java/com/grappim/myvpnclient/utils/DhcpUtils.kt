package com.grappim.myvpnclient.utils

import android.content.Context
import com.grappim.myvpnclient.core.extensions.getDhcpInfo
import timber.log.Timber
import java.math.BigInteger
import java.net.InetAddress
import java.net.UnknownHostException
import java.nio.ByteOrder

class DhcpUtils internal constructor(private val context: Context) {

  fun getDnsOne(): String = intToInetAddress(context.getDhcpInfo()?.dns1)

  fun getDnsTwo(): String = intToInetAddress(context.getDhcpInfo()?.dns2)

  fun getNetmask(): String = intToInetAddress(context.getDhcpInfo()?.netmask)

  fun getDhcpGateway(): String = intToInetAddress(context.getDhcpInfo()?.gateway)

  fun getDhcpLeaseDuration(): String = context.getDhcpInfo()?.leaseDuration.toString()

  /**
   * https://stackoverflow.com/questions/12001449/how-to-obtain-the-ip-address-of-the-connected-wifi-router-in-android-programmati
   */
  private fun getDhcpGatewayTest(): String {
    context.getDhcpInfo()?.gateway?.let {
      val gateway =
        if (ByteOrder.nativeOrder() == ByteOrder.LITTLE_ENDIAN)
          Integer.reverseBytes(it)
        else it
      val ipAddressByte =
        BigInteger.valueOf(gateway.toLong()).toByteArray()
      try {
        return InetAddress.getByAddress(ipAddressByte).hostAddress
      } catch (e: UnknownHostException) {
        Timber.d("Error getting Dhcp Gateway IP address ")
      }
    }
    return "0.0.0.0"
  }

  /**
   * https://stackoverflow.com/questions/5387036/programmatically-getting-the-gateway-and-subnet-mask-details
   * https://www.bennadel.com/blog/1830-converting-ip-addresses-to-and-from-integer-values-with-coldfusion.htm
   * Convert a IPv4 address from an integer to an InetAddress.
   * @param hostAddress an int corresponding to the IPv4 address in network byte order
   */
  private fun intToInetAddress(hostAddress: Int?): String {
    hostAddress?.let {
      val addressBytes = byteArrayOf(
        (0xff and hostAddress).toByte(),
        (0xff and (hostAddress shr 8)).toByte(),
        (0xff and (hostAddress shr 16)).toByte(),
        (0xff and (hostAddress shr 24)).toByte()
      )
      return try {
        InetAddress.getByAddress(addressBytes).hostAddress
      } catch (e: UnknownHostException) {
        "0.0.0.0"
      }
    }
    return "0.0.0.0"
  }
}
