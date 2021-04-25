package com.grappim.androHelper.ui.networkinfo

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.grappim.androHelper.R
import com.grappim.androHelper.core.functional.Resource
import com.grappim.androHelper.core.utils.ConnectivityNetwork
import com.grappim.androHelper.core.utils.DEFAULT_IP_ADDRESS
import com.grappim.androHelper.core.utils.DhcpUtils
import com.grappim.androHelper.core.utils.GeneralUtils
import com.grappim.androHelper.core.utils.REQUEST_CODE_READ_PHONE_STATE
import com.grappim.androHelper.core.utils.WifiUtils
import com.grappim.androHelper.entities.IpEntityDTO
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
        viewModel.externalIp.observe(viewLifecycleOwner, ::showIpInformation)
        initViews()
        refreshData()
    }

    private fun showIpInformation(resource: Resource<IpEntityDTO>) {
        when (resource) {
            is Resource.Error -> {
                textExternalIp.text = getString(
                    R.string.title_external_ip,
                    DEFAULT_IP_ADDRESS
                )
                Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show()
            }
            is Resource.Success -> {
                val ipEntityDTO = resource.data
                textExternalIp.text = getString(
                    R.string.title_external_ip,
                    ipEntityDTO.ip
                )
                textCity.text = getString(R.string.title_city, ipEntityDTO.location?.city)
                textRegion.text = getString(R.string.title_region, ipEntityDTO.location?.region)
                textCountry.text = getString(R.string.title_country, ipEntityDTO.location?.country)
                textLatitude.text =
                    getString(R.string.title_latitude, ipEntityDTO.location?.lat?.toString())
                textLongitude.text =
                    getString(R.string.title_longitude, ipEntityDTO.location?.lng?.toString())
                textIsp.text = getString(R.string.title_isp, ipEntityDTO.isp)
            }
        }
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
        viewModel.getExternalIp()

        setImei()
        textInternalIp.text = getString(
            R.string.title_internal_ip,
            connectivityNetwork.getInternalIpAddress()
        )
        textGateway.text = dhcpUtils.getDhcpGateway()
        textLeaseDuration.text = dhcpUtils.getDhcpLeaseDuration()
        textMacAddress.text = connectivityNetwork.getMacAddress()
        textConnectionType.text = connectivityNetwork.getNetworkClass()
        textType.text = "Type: "

        textSignal.text = ""
        textSsid.text = wifiUtils.getSsid()
        textBssid.text = wifiUtils.getBssid()
        textSpeed.text = wifiUtils.getLinkSpeed()
        textFrequency.text = getString(
            R.string.title_frequency,
            wifiUtils.getFrequency()
        )
        textNetworkId.text = getString(
            R.string.title_network_id,
            wifiUtils.getNetworkId()
        )

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
