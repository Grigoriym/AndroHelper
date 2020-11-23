package com.grappim.myvpnclient.vpn

import android.app.Service
import android.content.Intent
import android.net.VpnService
import android.os.ParcelFileDescriptor
import com.grappim.myvpnclient.core.utils.ConnectivityNetwork
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.selects.whileSelect
import org.pcap4j.packet.IpV4Packet
import org.pcap4j.packet.namednumber.IpNumber
import timber.log.Timber
import java.io.FileInputStream
import java.io.FileOutputStream
import java.nio.ByteBuffer

class MyLocalVpnService : VpnService() {

  companion object {
    const val COMMAND = "COMMAND"
    const val COMMAND_STOP = "STOP"
  }

  private val closeChannel = Channel<Unit>()
  private val inputChannel = Channel<IpV4Packet>()

  private var vpnInterface: ParcelFileDescriptor? = null

  private var udpVpnService: UdpVpnService? = null
  private var tcpVpnService: TcpVpnService? = null

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
    setupVpn()
    udpVpnService = UdpVpnService(this, inputChannel, closeChannel)
    udpVpnService?.start()

    tcpVpnService = TcpVpnService(this, inputChannel, closeChannel)
    tcpVpnService?.start()

    startVpn()

  }

  override fun onDestroy() {
    super.onDestroy()
    stopSelf()
  }

  suspend fun runVpnLoop() {
    var alive = true
    // Receive from local and send to remote network.
    val vpnInputStream = FileInputStream(vpnInterface!!.fileDescriptor).channel
    // Receive from remote and send to local network.
    val vpnOutputStream = FileOutputStream(vpnInterface!!.fileDescriptor).channel

    coroutineScope.launch {
      // TODO: should be take BufferPool.
      loop@ while (alive) {
        val buffer = ByteBuffer.allocate(1024)
        val readBytes = vpnInputStream.read(buffer)
        if (readBytes <= 0) {
          delay(100)
          continue@loop
        }
        val packet = IpV4Packet.newPacket(buffer.array(), 0, readBytes)
        when (packet.header.protocol) {
          IpNumber.UDP -> {
            udpVpnService!!.outputCh.send(packet)
          }
          IpNumber.TCP -> {
            tcpVpnService!!.outputCh.send(packet)
          }
          else -> {

          }
        }
      }
    }

    whileSelect {
      inputChannel.onReceive { value ->
        vpnOutputStream.write(ByteBuffer.wrap(value.rawData))
        true
      }
      closeChannel.onReceiveOrNull {
        false
      }
    }
    vpnInputStream.close()
    vpnOutputStream.close()
    alive = false
  }

  private fun setupVpn() {
    val builder = Builder()
      .addAddress("119.202.92.49", 24)
      .addDnsServer("8.8.8.8")
      .addRoute("0.0.0.0", 0)
      .setSession(javaClass.name)
    vpnInterface = builder.establish()
  }

  private fun startVpn() {
    coroutineScope.launch {
      runVpnLoop()
    }
  }

  private fun stopVpn() {
    parentJob.cancel()
    closeChannel.close()
    vpnInterface?.close()
    udpVpnService?.stop()
    tcpVpnService?.stop()
    stopSelf()
    Timber.d("grg ${javaClass.name} stopVpn")
  }

}