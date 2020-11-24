package com.grappim.myvpnclient.ui.vpn

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.grappim.myvpnclient.R
import com.grappim.myvpnclient.core.utils.ConnectivityNetwork
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_vpn.swipeRefresh
import kotlinx.android.synthetic.main.fragment_vpn.textIsVpnConnected
import javax.inject.Inject

@AndroidEntryPoint
class VpnFragment : Fragment(R.layout.fragment_vpn) {

    @Inject
    lateinit var connectivityNetwork: ConnectivityNetwork

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getData()
        swipeRefresh.setOnRefreshListener {
            getData()
            swipeRefresh.isRefreshing = false
        }
    }

    private fun getData() {
        textIsVpnConnected.text = getString(
            R.string.title_is_vpn_connected,
            connectivityNetwork.isVpnConnected().toString()
        )
    }

}
