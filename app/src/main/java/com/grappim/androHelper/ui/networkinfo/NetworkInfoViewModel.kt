package com.grappim.androHelper.ui.networkinfo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grappim.androHelper.core.functional.Resource
import com.grappim.androHelper.core.functional.onFailure
import com.grappim.androHelper.core.functional.onSuccess
import com.grappim.androHelper.domain.ip.GetIpUseCase
import com.grappim.androHelper.data.remote.model.ip.IpEntityDTO
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NetworkInfoViewModel @Inject constructor(
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