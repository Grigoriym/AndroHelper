package com.grappim.myvpnclient.ui

import com.grappim.myvpnclient.entities.IpEntity

interface MainContract {
  interface View {
    fun getFullIpInformationSuccess(ipEntity: IpEntity)
    fun getFullIpInformationFailure()
  }

  interface Presenter {
    fun setView(view: View)
    fun destroy()
    fun getFullIpInformation()
  }
}