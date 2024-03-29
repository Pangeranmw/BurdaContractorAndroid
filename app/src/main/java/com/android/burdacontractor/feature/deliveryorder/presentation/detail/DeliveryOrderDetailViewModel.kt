package com.android.burdacontractor.feature.deliveryorder.presentation.detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import com.android.burdacontractor.core.data.Resource
import com.android.burdacontractor.core.domain.model.Event
import com.android.burdacontractor.core.domain.model.enums.StateResponse
import com.android.burdacontractor.core.utils.LiveNetworkChecker
import com.android.burdacontractor.feature.deliveryorder.domain.model.DeliveryOrderDetailItem
import com.android.burdacontractor.feature.deliveryorder.domain.usecase.DeleteDeliveryOrderUseCase
import com.android.burdacontractor.feature.deliveryorder.domain.usecase.GetDeliveryOrderByIdUseCase
import com.android.burdacontractor.feature.deliveryorder.domain.usecase.MarkCompleteDeliveryOrderUseCase
import com.android.burdacontractor.feature.deliveryorder.domain.usecase.SendDeliveryOrderUseCase
import com.android.burdacontractor.feature.profile.data.source.remote.response.UserByTokenItem
import com.android.burdacontractor.feature.profile.domain.usecase.GetUserByTokenUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DeliveryOrderDetailViewModel @Inject constructor(
    val liveNetworkChecker: LiveNetworkChecker,
    private val getDeliveryOrderByIdUseCase: GetDeliveryOrderByIdUseCase,
    private val getUserByTokenUseCase: GetUserByTokenUseCase,
    private val deleteDeliveryOrderUseCase: DeleteDeliveryOrderUseCase,
    private val sendDeliveryOrderUseCase: SendDeliveryOrderUseCase,
    private val markCompleteDeliveryOrderUseCase: MarkCompleteDeliveryOrderUseCase
) : ViewModel() {

    private val _state = MutableLiveData<StateResponse?>()
    val state: LiveData<StateResponse?> = _state

    private val _user = MutableLiveData<UserByTokenItem>()
    val user: LiveData<UserByTokenItem> = _user

    private val _id = MutableLiveData<String?>(null)
    val id: LiveData<String?> = _id

    private val _messageResponse = MutableLiveData<Event<String?>>()
    val messageResponse : LiveData<Event<String?>> = _messageResponse

    private val _deliveryOrder = MutableLiveData<DeliveryOrderDetailItem>()
    val deliveryOrder : LiveData<DeliveryOrderDetailItem> = _deliveryOrder

    init {
        getUserByToken()
        viewModelScope.launch {
            _id.asFlow().collect {
                it?.let { id ->
                    getDeliveryOrderById(id)
                }
            }
        }
    }
    fun setId(id: String){
        _id.value = id
    }
    private fun getUserByToken(){
        viewModelScope.launch {
            getUserByTokenUseCase.execute().collect{
                when(it){
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

    fun sendDeliveryOrder(id: String, listener: () -> Unit) {
        viewModelScope.launch {
            sendDeliveryOrderUseCase.execute(id).collect {
                when (it) {
                    is Resource.Loading -> _state.value = StateResponse.LOADING
                    is Resource.Success -> {
                        _state.value = StateResponse.SUCCESS
                        _messageResponse.value = Event(it.message)
                        listener()
                    }

                    is Resource.Error -> {
                        _state.value = StateResponse.ERROR
                        _messageResponse.value = Event(it.message)
                    }
                }
            }
        }
    }

    fun markCompleteDeliveryOrder(id: String, listener: () -> Unit) {
        viewModelScope.launch {
            markCompleteDeliveryOrderUseCase.execute(id).collect {
                when (it) {
                    is Resource.Loading -> _state.value = StateResponse.LOADING
                    is Resource.Success -> {
                        _state.value = StateResponse.SUCCESS
                        _messageResponse.value = Event(it.message)
                        listener()
                    }

                    is Resource.Error -> {
                        _state.value = StateResponse.ERROR
                        _messageResponse.value = Event(it.message)
                    }
                }
            }
        }
    }

    fun deleteDeliveryOrder(id: String, listener: () -> Unit) {
        viewModelScope.launch {
            deleteDeliveryOrderUseCase.execute(id).collect {
                when (it) {
                    is Resource.Loading -> _state.value = StateResponse.LOADING
                    is Resource.Success -> {
                        _state.value = StateResponse.SUCCESS
                        _messageResponse.value = Event(it.message)
                        Log.d("deleteDeliveryOrder success", it.data?.message.toString())
                        listener()
                    }

                    is Resource.Error -> {
                        _state.value = StateResponse.ERROR
                        _messageResponse.value = Event(it.message)
                        Log.d("deleteDeliveryOrder error", it.data?.message.toString())
                    }
                }
            }
        }
    }

    fun getDeliveryOrderById(id: String) {
        viewModelScope.launch {
            getDeliveryOrderByIdUseCase.execute(id).collect { res ->
                when (res) {
                    is Resource.Loading -> _state.value = StateResponse.LOADING
                    is Resource.Success -> {
                        _state.value = StateResponse.SUCCESS
                        _deliveryOrder.value = res.data!!
                    }

                    is Resource.Error -> {
                        _state.value = StateResponse.ERROR
                    }
                }
            }
        }
    }
}



