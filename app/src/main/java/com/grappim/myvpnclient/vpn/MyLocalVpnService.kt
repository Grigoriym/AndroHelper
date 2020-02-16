package com.grappim.myvpnclient.vpn

import android.app.Service
import android.content.Intent
import android.net.VpnService
import android.os.ParcelFileDescriptor
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import org.pcap4j.packet.IpV4Packet
import timber.log.Timber

class MyLocalVpnService : VpnService() {

  companion object {
    const val COMMAND = "COMMAND"
    const val COMMAND_STOP = "STOP"
  }

  private val closeChannel = Channel<Unit>()
  private val inputChannel = Channel<IpV4Packet>()

  private var vpnInterface: ParcelFileDescriptor? = null

  private val parentJob = Job()
  private val coroutineScope = CoroutineScope(Dispatchers.Main + parentJob)

  override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
    if (intent?.getStringExtra(COMMAND) == COMMAND_STOP) {
      stopVpn()
    }
    return Service.START_STICKY
  }

  override fun onCreate() {
    super.onCreate()
  }

  override fun onDestroy() {
    super.onDestroy()
  }

  suspend fun vpnRunLoop() {

  }

  private fun setupVpn() {
    val builder = Builder()
      .addAddress("10.0.2.15", 24)
      .addDnsServer("8.8.8.8")
      .addRoute("0.0.0.0", 0)
      .setSession(javaClass.name)
    vpnInterface = builder.establish()

  }

  private fun startVpn() {
    coroutineScope.launch {
      vpnRunLoop()
    }
  }

  private fun stopVpn() {
    parentJob.cancel()
    closeChannel.close()
    vpnInterface?.close()
    stopSelf()
    Timber.d("grg ${javaClass.name} stopVpn")
  }

}