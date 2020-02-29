package com.grappim.myvpnclient.ui.networkinfo

import com.grappim.myvpnclient.entities.IpEntity

interface NetworkInfoContract {
  interface View {
    fun getFullIpInformationSuccess(ipEntity: IpEntity)
    fun getFullIpInformationFailure()
  }

  interface Presenter {
    fun setView(view: NetworkInfoContract.View)
    fun destroy()
    fun getFullIpInformation()
  }
}