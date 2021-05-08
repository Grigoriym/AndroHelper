package com.grappim.androHelper.ui.networkinfo.main

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.grappim.androHelper.R
import com.grappim.androHelper.databinding.FragmentNetworkMainBinding

class NetworkMainFragment : Fragment(R.layout.fragment_network_main) {

    private val viewBinding: FragmentNetworkMainBinding by viewBinding(FragmentNetworkMainBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}