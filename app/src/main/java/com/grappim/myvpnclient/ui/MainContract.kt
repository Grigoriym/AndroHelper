package com.grappim.myvpnclient.ui

interface MainContract {
  interface View {

  }

  interface Presenter {
    fun setView(view: View)
    fun destroy()
  }
}