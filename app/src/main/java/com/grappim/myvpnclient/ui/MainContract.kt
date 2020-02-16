package com.grappim.myvpnclient.ui

interface MainContract {
  interface View {
    fun getExternalIpSuccess(ip: String)
    fun getExternalIpFailure()
  }

  interface Presenter {
    fun setView(view: View)
    fun destroy()
    fun getExternalIp()
  }
}