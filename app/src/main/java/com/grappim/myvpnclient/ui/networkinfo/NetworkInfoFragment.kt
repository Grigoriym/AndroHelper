package com.grappim.myvpnclient.ui.networkinfo

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.grappim.myvpnclient.R
import com.grappim.myvpnclient.core.extensions.doOnInternet
import com.grappim.myvpnclient.core.utils.ConnectivityNetwork
import com.grappim.myvpnclient.core.utils.DhcpUtils
import com.grappim.myvpnclient.core.utils.GeneralUtils
import com.grappim.myvpnclient.core.utils.REQUEST_CODE_READ_PHONE_STATE
import com.grappim.myvpnclient.core.utils.WifiUtils
import com.grappim.myvpnclient.entities.IpEntity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_network_info.swipeRefresh
import kotlinx.android.synthetic.main.fragment_network_info.textBssid
import kotlinx.android.synthetic.main.fragment_network_info.textCity
import kotlinx.android.synthetic.main.fragment_network_info.textConnectionType
import kotlinx.android.synthetic.main.fragment_network_info.textCountry
import kotlinx.android.synthetic.main.fragment_network_info.textDns1
import kotlinx.android.synthetic.main.fragment_network_info.textDns2
import kotlinx.android.synthetic.main.fragment_network_info.textExternalIp
import kotlinx.android.synthetic.main.fragment_network_info.textFrequency
import kotlinx.android.synthetic.main.fragment_network_info.textGateway
import kotlinx.android.synthetic.main.fragment_network_info.textImei
import kotlinx.android.synthetic.main.fragment_network_info.textInternalIp
import kotlinx.android.synthetic.main.fragment_network_info.textIsp
import kotlinx.android.synthetic.main.fragment_network_info.textLatitude
import kotlinx.android.synthetic.main.fragment_network_info.textLeaseDuration
import kotlinx.android.synthetic.main.fragment_network_info.textLongitude
import kotlinx.android.synthetic.main.fragment_network_info.textMacAddress
import kotlinx.android.synthetic.main.fragment_network_info.textMask
import kotlinx.android.synthetic.main.fragment_network_info.textNetworkId
import kotlinx.android.synthetic.main.fragment_network_info.textRegion
import kotlinx.android.synthetic.main.fragment_network_info.textSignal
import kotlinx.android.synthetic.main.fragment_network_info.textSpeed
import kotlinx.android.synthetic.main.fragment_network_info.textSsid
import kotlinx.android.synthetic.main.fragment_network_info.textType
import javax.inject.Inject

@AndroidEntryPoint
class NetworkInfoFragment : Fragment(R.layout.fragment_network_info) {

    @Inject
    lateinit var connectivityNetwork: ConnectivityNetwork

    @Inject
    lateinit var dhcpUtils: DhcpUtils

    @Inject
    lateinit var wifiUtils: WifiUtils

    private val viewModel: NetworkInfoViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.externalIp.observe(
            viewLifecycleOwner
        ) {
            getFullIpInformationSuccess(it)
        }
        initViews()
        refreshData()
    }

    fun getFullIpInformationSuccess(ipEntity: IpEntity) {
        textExternalIp.text = ipEntity.ip
        textCity.text = getString(R.string.title_city, ipEntity.location?.city)
        textRegion.text = getString(R.string.title_region, ipEntity.location?.region)
        textCountry.text = getString(R.string.title_country, ipEntity.location?.country)
        textLatitude.text = getString(R.string.title_latitude, ipEntity.location?.lat?.toString())
        textLongitude.text = getString(R.string.title_longitude, ipEntity.location?.lng?.toString())
        textIsp.text = getString(R.string.title_isp, ipEntity.isp)
    }

    fun getFullIpInformationFailure() {
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
            viewModel.getExternalIp()
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
