package com.android.burdacontractor.feature.deliveryorder.presentation.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.android.burdacontractor.core.data.Resource
import com.android.burdacontractor.core.domain.model.Event
import com.android.burdacontractor.core.domain.model.enums.CreatedByOrFor
import com.android.burdacontractor.core.domain.model.enums.DeliveryOrderStatus
import com.android.burdacontractor.core.domain.model.enums.StateResponse
import com.android.burdacontractor.core.utils.LiveNetworkChecker
import com.android.burdacontractor.feature.deliveryorder.domain.model.AllDeliveryOrder
import com.android.burdacontractor.feature.deliveryorder.domain.usecase.GetAllDeliveryOrderUseCase
import com.android.burdacontractor.feature.profile.data.source.remote.response.UserByTokenItem
import com.android.burdacontractor.feature.profile.domain.usecase.GetUserByTokenUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DeliveryOrderViewModel @Inject constructor(
    val liveNetworkChecker: LiveNetworkChecker,
    private val getAllDeliveryOrderUseCase: GetAllDeliveryOrderUseCase,
    private val getUserByTokenUseCase: GetUserByTokenUseCase,
) : ViewModel() {
    private val _state = MutableLiveData<StateResponse?>()
    val state: LiveData<StateResponse?> = _state

    private val _dateStart = MutableLiveData<String?>()
    val dateStart: LiveData<String?> = _dateStart

    private val _dateEnd = MutableLiveData<String?>()
    val dateEnd: LiveData<String?> = _dateEnd

    private val _createdByOrFor = MutableLiveData(CreatedByOrFor.all)
    val createdByOrFor: LiveData<CreatedByOrFor> = _createdByOrFor

    private val _status = MutableLiveData(DeliveryOrderStatus.SEMUA)
    val status: LiveData<DeliveryOrderStatus?> = _status

    private val _search = MutableLiveData<String?>(null)
    val search: LiveData<String?> = _search

    private val _messageResponse = MutableLiveData<Event<String?>>()
    val messageResponse: LiveData<Event<String?>> = _messageResponse

    private val _user = MutableLiveData<UserByTokenItem>()
    val user: LiveData<UserByTokenItem> = _user

    init {
        getUserByToken()
    }

    fun getUserByToken() {
        viewModelScope.launch {
            getUserByTokenUseCase.execute().collect {
                when (it) {
                    is Resource.Loading -> _state.value = StateResponse.LOADING
                    is Resource.Success -> {
                        _user.value = it.data!!
                    }

                    is Resource.Error -> {
                        _state.value = StateResponse.ERROR
                    }
                }
            }
        }
    }

    fun setStatus(status: DeliveryOrderStatus) {
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
            size = 30,
            status = _status.value!!,
            search = _search.value,
            dateStart = _dateStart.value,
            dateEnd = _dateEnd.value,
            createdByOrFor = _createdByOrFor.value!!,
        ).cachedIn(viewModelScope)
    }
}



