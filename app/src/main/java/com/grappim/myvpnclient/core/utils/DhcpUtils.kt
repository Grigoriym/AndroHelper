package com.grappim.myvpnclient.core.utils

import android.net.DhcpInfo
import timber.log.Timber
import java.math.BigInteger
import java.net.InetAddress
import java.net.UnknownHostException
import java.nio.ByteOrder
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DhcpUtils @Inject constructor(
    private val dhcpInfo: DhcpInfo
) {

    fun getDnsOne(): String = intToInetAddress(dhcpInfo.dns1)

    fun getDnsTwo(): String = intToInetAddress(dhcpInfo.dns2)

    fun getNetmask(): String = intToInetAddress(dhcpInfo.netmask)

    fun getDhcpGateway(): String = intToInetAddress(dhcpInfo.gateway)

    fun getDhcpLeaseDuration(): String = dhcpInfo.leaseDuration.toString()

    /**
     * https://stackoverflow.com/questions/12001449/how-to-obtain-the-ip-address-of-the-connected-wifi-router-in-android-programmati
     */
    private fun getDhcpGatewayTest(): String {
        dhcpInfo.gateway.let {
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
        return DEFAULT_IP_ADDRESS
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
                DEFAULT_IP_ADDRESS
            }
        }
        return DEFAULT_IP_ADDRESS
    }
}
