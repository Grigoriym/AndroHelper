package com.grappim.androHelper.ui.networkinfo.ip

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.grappim.androHelper.core.executor.CoroutineContextProvider
import com.grappim.androHelper.core.functional.Resource
import com.grappim.androHelper.core.functional.onFailure
import com.grappim.androHelper.core.functional.onSuccess
import com.grappim.androHelper.data.remote.model.ip.IpInfoDTO
import com.grappim.androHelper.domain.ip.GetIpUseCase
import com.grappim.androHelper.core.network.NetworkStatusTracker
import com.grappim.androHelper.core.network.map
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NetworkInfoViewModel @Inject constructor(
    private val getIpUseCase: GetIpUseCase,
    private val networkStatusTracker: NetworkStatusTracker,
    private val coroutineContextProvider: CoroutineContextProvider
) : ViewModel() {

    val state = networkStatusTracker.networkStatus
        .map(
            onAvailable = { },
            onUnavailable = { },
        )
        .asLiveData(coroutineContextProvider.io)

    private val _externalIp = MutableLiveData<Resource<GetIpUseCase.IpInfo>>()
    val externalIp: LiveData<Resource<GetIpUseCase.IpInfo>>
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