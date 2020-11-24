package com.grappim.myvpnclient.ui.networkinfo

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grappim.myvpnclient.core.functional.Resource
import com.grappim.myvpnclient.core.functional.onFailure
import com.grappim.myvpnclient.core.functional.onSuccess
import com.grappim.myvpnclient.entities.IpEntityDTO
import kotlinx.coroutines.launch

class NetworkInfoViewModel @ViewModelInject constructor(
    private val getIpUseCase: GetIpUseCase
) : ViewModel() {

    private val _externalIp = MutableLiveData<Resource<IpEntityDTO>>()
    val externalIp: LiveData<Resource<IpEntityDTO>>
        get() = _externalIp

    fun getExternalIp() {
        viewModelScope.launch {
            getIpUseCase.invoke()
                .onFailure {
                    _externalIp.value = Resource.Error(it)
                }.onSuccess {
                    _externalIp.value = Resource.Success(it)
                }
        }
    }

}