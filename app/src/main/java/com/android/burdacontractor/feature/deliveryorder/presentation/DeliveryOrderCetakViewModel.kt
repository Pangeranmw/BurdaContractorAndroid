package com.android.burdacontractor.feature.deliveryorder.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.burdacontractor.core.domain.model.enums.StateResponse
import com.android.burdacontractor.core.utils.LiveNetworkChecker
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DeliveryOrderCetakViewModel @Inject constructor(
    val liveNetworkChecker: LiveNetworkChecker,
    ) : ViewModel() {

    private val _state = MutableLiveData<StateResponse?>()
    val state: LiveData<StateResponse?> = _state
}



