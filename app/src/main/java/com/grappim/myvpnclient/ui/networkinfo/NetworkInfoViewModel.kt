package com.grappim.myvpnclient.ui.networkinfo

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grappim.myvpnclient.entities.IpEntity
import kotlinx.coroutines.launch

class NetworkInfoViewModel @ViewModelInject constructor(
    private val getIpUseCase: GetIpUseCase
) : ViewModel() {

    private val _externalIp = MutableLiveData<IpEntity>()
    val externalIp: LiveData<IpEntity>
        get() = _externalIp

    fun getExternalIp() {
        viewModelScope.launch {
            _externalIp.value = getIpUseCase.invoke()
        }
    }

}