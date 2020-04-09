package com.grappim.myvpnclient.ui.networkinfo

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.grappim.myvpnclient.R
import com.grappim.myvpnclient.core.extensions.doOnInternet
import com.grappim.myvpnclient.core.utils.*
import com.grappim.myvpnclient.entities.IpEntity
import kotlinx.android.synthetic.main.fragment_network_info.*
import org.koin.android.ext.android.inject

class NetworkInfoFragment : Fragment(), NetworkInfoContract.View {

  private val presenter: NetworkInfoPresenter by inject()

  private val connectivityNetwork: ConnectivityNetwork by inject()
  private val dhcpUtils: DhcpUtils by inject()
  private val wifiUtils: WifiUtils by inject()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    presenter.setView(this)
  }

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? =
    inflater.inflate(R.layout.fragment_network_info, container, false)

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    initViews()
    refreshData()
  }

  override fun getFullIpInformationSuccess(ipEntity: IpEntity) {
    textExternalIp.text = ipEntity.ip
    textCity.text = getString(R.string.title_city, ipEntity.location?.city)
    textRegion.text = getString(R.string.title_region, ipEntity.location?.region)
    textCountry.text = getString(R.string.title_country, ipEntity.location?.country)
    textLatitude.text = getString(R.string.title_latitude, ipEntity.location?.lat?.toString())
    textLongitude.text = getString(R.string.title_longitude, ipEntity.location?.lng?.toString())
    textIsp.text = getString(R.string.title_isp, ipEntity.isp)
  }

  override fun getFullIpInformationFailure() {
    textExternalIp.text = "0"
  }

  override fun onRequestPermissionsResult(
    requestCode: Int,
    permissions: Array<out String>,
    grantResults: IntArray
  ) {
    when (requestCode) {
      REQUEST_CODE_READ_PHONE_STATE -> {
        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
          setImei()
        }
      }
    }
  }

  @SuppressLint("MissingPermission")
  private fun refreshData() {
    requireContext().doOnInternet({
      presenter.getFullIpInformation()
    }, {
      getFullIpInformationFailure()
      Toast.makeText(requireContext(), "Internet not Connected", Toast.LENGTH_SHORT).show()
    })

    setImei()
    textInternalIp.text = connectivityNetwork.getInternalIpAddress()
    textGateway.text = dhcpUtils.getDhcpGateway()
    textLeaseDuration.text = dhcpUtils.getDhcpLeaseDuration()
    textMacAddress.text = connectivityNetwork.getMacAddress()
    textConnectionType.text = connectivityNetwork.getNetworkClass()
    textType.text = "Type: "

    textSignal.text = ""
    textSsid.text = wifiUtils.getSsid()
    textBssid.text = wifiUtils.getBssid()
    textSpeed.text = wifiUtils.getLinkSpeed()
    textFrequency.text = "Frequency: ${wifiUtils.getFrequency()}"
    textNetworkId.text = "Network Id: ${wifiUtils.getNetworkId()}"

    textDns1.text = getString(R.string.title_dns_one, dhcpUtils.getDnsOne())
    textDns2.text = getString(R.string.title_dns_two, dhcpUtils.getDnsTwo())
    textMask.text = getString(R.string.title_mask, dhcpUtils.getNetmask())
  }

  private fun initViews() {
    swipeRefresh.setOnRefreshListener {
      refreshData()
      swipeRefresh.isRefreshing = false
    }
  }

  private fun setImei() {
    if (GeneralUtils.checkReadPhoneStatePermission(requireContext())) {
      textImei.text = GeneralUtils.getImei(requireContext())
    } else {
      ActivityCompat.requestPermissions(
        requireActivity(),
        arrayOf(Manifest.permission.READ_PHONE_STATE),
        REQUEST_CODE_READ_PHONE_STATE
      )
    }
  }

}
