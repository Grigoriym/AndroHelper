package com.grappim.myvpnclient.ui

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.VpnService
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.grappim.myvpnclient.R
import com.grappim.myvpnclient.utils.*
import com.grappim.myvpnclient.vpn.MyLocalVpnService
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.ext.android.inject
import timber.log.Timber

class MainActivity : AppCompatActivity(), MainContract.View {

  private val presenter: MainPresenter by inject()
  private val connectivityNetwork: ConnectivityNetwork by inject()
  private val dhcpUtils: DhcpUtils by inject()

  private val networkChangeReceiver = object : NetworkChangeReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
      super.onReceive(context, intent)
      doOnInternet({
        refreshData()
      }, {

      })
    }
  }
  private val intentFilterNetwork = IntentFilter("android.net.conn.CONNECTIVITY_CHANGE")

  companion object {
    const val VPN_REQUEST_CODE = 421232
    const val LOCATION_PERMISSION_CODE = 9665
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    presenter.setView(this)
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    buttonStart.setSafeOnClickListener {
      doOnInternet({
        //        startVpn()
      }, {

      })
    }
    buttonEnd.setSafeOnClickListener {
      //      endVpn()
    }
    swipeRefresh.setOnRefreshListener {
      refreshData()
      swipeRefresh.isRefreshing = false
    }
  }

  override fun onResume() {
    checkPermissions()
    registerReceiver(networkChangeReceiver, intentFilterNetwork)
    super.onResume()
    refreshData()
  }

  override fun onDestroy() {
    unregisterReceiver(networkChangeReceiver)
    super.onDestroy()
  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)
    if (requestCode == VPN_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
      val intent = Intent(this, MyLocalVpnService::class.java)
      startService(intent)
    }
  }

  override fun getExternalIpSuccess(ip: String) {
    textExternalIp.text = ip
  }

  override fun getExternalIpFailure() {
    textExternalIp.text = "0"
  }

  private fun checkPermissions() {
    if (ActivityCompat
        .checkSelfPermission(
          this,
          Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED
    ) {
      ActivityCompat.requestPermissions(
        this,
        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
        LOCATION_PERMISSION_CODE
      )
    } else {

    }
  }

  @SuppressLint("MissingPermission")
  private fun refreshData() {
    doOnInternet({
      presenter.getExternalIp()
    }, {
      getExternalIpFailure()
      Toast.makeText(this, "Internet not Connected", Toast.LENGTH_SHORT).show()
    })

    textInternalIp.text = connectivityNetwork.getInternalIpAddress()
    textGateway.text = dhcpUtils.getDhcpGateway()
    textLeaseDuration.text = dhcpUtils.getDhcpLeaseDuration()
    textMacAddress.text = connectivityNetwork.getMacAddress()
    textConnectionType.text = connectivityNetwork.getNetworkClass()

    textSignal.text = ""
    textSsid.text = connectivityNetwork.getSsid()
    textBssid.text = connectivityNetwork.getBssid()
    textSpeed.text = ""

    textDns1.text = getString(R.string.title_dns_one, dhcpUtils.getDnsOne())
    textDns2.text = getString(R.string.title_dns_two, dhcpUtils.getDnsTwo())
    textMask.text = getString(R.string.title_mask, dhcpUtils.getNetmask())
  }

  private fun startVpn() {
    Timber.d("click startVpn")
    val intent = VpnService.prepare(this)
    intent?.let {
      startActivityForResult(intent, VPN_REQUEST_CODE)
    } ?: onActivityResult(VPN_REQUEST_CODE, Activity.RESULT_OK, null)
  }

  private fun endVpn() {
    Timber.d("click endVpn")
    val intent = Intent(this, MyLocalVpnService::class.java)
    intent.putExtra(MyLocalVpnService.COMMAND, MyLocalVpnService.COMMAND_STOP)
    startService(intent)
  }
}
