package com.grappim.myvpnclient.ui

import com.grappim.myvpnclient.api.MixService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import org.koin.core.KoinComponent
import org.koin.core.inject

class MainPresenter : MainContract.Presenter, KoinComponent {

  private val service: MixService by inject()

  private var mView: MainContract.View? = null
  private var disposable = CompositeDisposable()

  override fun setView(view: MainContract.View) {
    mView = view
  }

  override fun destroy() {
    mView = null
    disposable.clear()
  }

  override fun getFullIpInformation() {
    disposable.add(
      service.getExternalIp()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe({
          mView?.getFullIpInformationSuccess(it)
        }, {
          it.printStackTrace()
        })
    )
  }
}