package com.android.burdacontractor.feature.deliveryorder.presentation.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.android.burdacontractor.core.domain.model.Event
import com.android.burdacontractor.core.domain.model.enums.CreatedByOrFor
import com.android.burdacontractor.core.domain.model.enums.DeliveryOrderStatus
import com.android.burdacontractor.core.domain.model.enums.StateResponse
import com.android.burdacontractor.core.utils.LiveNetworkChecker
import com.android.burdacontractor.feature.deliveryorder.domain.model.AllDeliveryOrder
import com.android.burdacontractor.feature.deliveryorder.domain.usecase.GetAllDeliveryOrderUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DeliveryOrderViewModel @Inject constructor(
    val liveNetworkChecker: LiveNetworkChecker,
    private val getAllDeliveryOrderUseCase: GetAllDeliveryOrderUseCase,
) : ViewModel() {
    private val _state = MutableLiveData<StateResponse?>()
    val state: LiveData<StateResponse?> = _state

    private val _dateStart = MutableLiveData<String?>()
    val dateStart: LiveData<String?> = _dateStart

    private val _dateEnd = MutableLiveData<String?>()
    val dateEnd: LiveData<String?> = _dateEnd

    private val _createdByOrFor = MutableLiveData(CreatedByOrFor.all)
    val createdByOrFor: LiveData<CreatedByOrFor> = _createdByOrFor

    private val _status = MutableLiveData(DeliveryOrderStatus.MENUNGGU_KONFIRMASI_DRIVER)
    val status: LiveData<DeliveryOrderStatus?> = _status

    private val _search = MutableLiveData<String?>(null)
    val search: LiveData<String?> = _search

    private val _messageResponse = MutableLiveData<Event<String?>>()
    val messageResponse : LiveData<Event<String?>> = _messageResponse

    init{
        getAllDelivery()
    }
    fun setStatus(status: DeliveryOrderStatus){
        _status.value = status
    }

    fun setState(state: StateResponse) {
        _state.value = state
    }

    fun setCreatedByOrFor(createdByOrFor: CreatedByOrFor) {
        _createdByOrFor.value = createdByOrFor
    }

    fun setSearch(search: String) {
        _search.value = search
    }

    fun setDate(dateStart: String?, dateEnd: String?) {
        _dateStart.value = dateStart
        _dateEnd.value = dateEnd
    }

    fun getAllDelivery(): LiveData<PagingData<AllDeliveryOrder>> {
        return getAllDeliveryOrderUseCase.execute(
            status = _status.value!!,
            search = _search.value,
            dateStart = _dateStart.value,
            dateEnd = _dateEnd.value,
            createdByOrFor = _createdByOrFor.value!!,
        ).cachedIn(viewModelScope)
    }
}



