package com.android.burdacontractor.feature.deliveryorder.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.android.burdacontractor.core.data.Resource
import com.android.burdacontractor.core.domain.model.Event
import com.android.burdacontractor.core.domain.model.enums.StateResponse
import com.android.burdacontractor.core.domain.model.enums.SuratJalanStatus
import com.android.burdacontractor.core.domain.model.enums.SuratJalanTipe
import com.android.burdacontractor.core.utils.LiveNetworkChecker
import com.android.burdacontractor.feature.deliveryorder.data.source.remote.response.DeliveryOrderDetailItem
import com.android.burdacontractor.feature.deliveryorder.domain.model.DeliveryOrderDetail
import com.android.burdacontractor.feature.deliveryorder.domain.usecase.GetDeliveryOrderByIdUseCase
import com.android.burdacontractor.feature.suratjalan.domain.model.AllSuratJalan
import com.android.burdacontractor.feature.suratjalan.domain.usecase.GetAllSuratJalanUseCase
import com.android.burdacontractor.feature.suratjalan.domain.usecase.GetCountActiveSuratJalanUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DeliveryOrderDetailViewModel @Inject constructor(
    val liveNetworkChecker: LiveNetworkChecker,
    private val getDeliveryOrderByIdUseCase: GetDeliveryOrderByIdUseCase,
) : ViewModel() {

    private val _state = MutableLiveData<StateResponse?>()
    val state: LiveData<StateResponse?> = _state

    private val _messageResponse = MutableLiveData<Event<String?>>()
    val messageResponse : LiveData<Event<String?>> = _messageResponse

    private val _deliveryOrder = MutableLiveData<DeliveryOrderDetailItem>()
    val deliveryOrder : LiveData<DeliveryOrderDetailItem> = _deliveryOrder

    fun getDeliveryOrderById(id:String){
        viewModelScope.launch {
            getDeliveryOrderByIdUseCase.execute(id).collect{
                when(it){
                    is Resource.Loading -> _state.value = StateResponse.LOADING
                    is Resource.Success -> {
                        _state.value = StateResponse.SUCCESS
                        _deliveryOrder.value = it.data!!
                    }
                    is Resource.Error -> {
                        _state.value = StateResponse.ERROR
                    }
                }
            }
        }
    }
}



