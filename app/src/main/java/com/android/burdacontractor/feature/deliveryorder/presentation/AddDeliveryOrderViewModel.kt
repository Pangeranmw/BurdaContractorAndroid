package com.android.burdacontractor.feature.deliveryorder.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.burdacontractor.core.domain.model.Event
import com.android.burdacontractor.core.domain.model.enums.StateResponse
import com.android.burdacontractor.core.utils.LiveNetworkChecker
import com.android.burdacontractor.feature.deliveryorder.data.source.remote.request.CreateStepOneDeliveryOrderBody
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AddDeliveryOrderViewModel @Inject constructor(
    val liveNetworkChecker: LiveNetworkChecker,
) : ViewModel() {
    private val _state = MutableLiveData<StateResponse?>()
    val state: LiveData<StateResponse?> = _state

    private val _canSwipe = MutableLiveData(false)
    val canSwipe: LiveData<Boolean> = _canSwipe

    private val _addDoStepOne = MutableLiveData<CreateStepOneDeliveryOrderBody?>(null)
    val addDoStepOne: LiveData<CreateStepOneDeliveryOrderBody?> = _addDoStepOne

    private val _messageResponse = MutableLiveData<Event<String?>>()
    val messageResponse: LiveData<Event<String?>> = _messageResponse

    fun setCanSwipe(canSwipe: Boolean) {
        _canSwipe.value = canSwipe
    }

    fun setAddDoStepOne(addDoStepOne: CreateStepOneDeliveryOrderBody) {
        _addDoStepOne.value = addDoStepOne
    }

    init {

    }
}



