package com.grappim.myvpnclient.ui

import android.app.Activity
import android.content.Intent
import android.net.VpnService
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.grappim.myvpnclient.R
import com.grappim.myvpnclient.utils.ConnectivityNetwork
import com.grappim.myvpnclient.utils.doOnInternet
import com.grappim.myvpnclient.utils.setSafeOnClickListener
import com.grappim.myvpnclient.vpn.MyLocalVpnService
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.ext.android.inject
import timber.log.Timber

class MainActivity : AppCompatActivity(), MainContract.View {

  private val presenter: MainPresenter by inject()
  private val connectivityNetwork: ConnectivityNetwork by inject()

  companion object {
    const val VPN_REQUEST_CODE = 421232
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    presenter.setView(this)
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    refreshData()

    buttonStart.setSafeOnClickListener {
      doOnInternet({
        startVpn()
      }, {

      })
    }
    buttonEnd.setSafeOnClickListener {
      doOnInternet({
        endVpn()
      }, {

      })
    }
    swipeRefresh.setOnRefreshListener {
      refreshData()
      swipeRefresh.isRefreshing = false
    }
  }

  override fun onResume() {
    super.onResume()
    refreshData()
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
  }

  private fun refreshData() {
    doOnInternet({
      presenter.getExternalIp()
      textInternalIp.text = connectivityNetwork.getInternalIpAddress()
      textGateway.text = connectivityNetwork.getDhcpGateway()
      textLeaseDuration.text = connectivityNetwork.getDhcpLeaseDuration()
      textMacAddress.text = connectivityNetwork.getMacAddress()
      textConnectionType.text = connectivityNetwork.getNetworkClass()
    }, {
      Toast.makeText(this, "Internet not Connected", Toast.LENGTH_SHORT).show()
    })
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
