package com.android.burdacontractor.feature.deliveryorder.presentation.create

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.burdacontractor.core.domain.model.Event
import com.android.burdacontractor.core.domain.model.enums.StateResponse
import com.android.burdacontractor.core.utils.LiveNetworkChecker
import com.android.burdacontractor.feature.deliveryorder.data.source.remote.request.CreateStepOneDeliveryOrderBody
import com.android.burdacontractor.feature.perusahaan.domain.model.AllPerusahaan
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AddDeliveryOrderViewModel @Inject constructor(
    val liveNetworkChecker: LiveNetworkChecker,
) : ViewModel() {
    init {

    }

    private val _state = MutableLiveData<StateResponse?>()
    val state: LiveData<StateResponse?> = _state

    private val _canSwipe = MutableLiveData(false)
    val canSwipe: LiveData<Boolean> = _canSwipe

    private val _addDoStepOne = MutableLiveData<CreateStepOneDeliveryOrderBody?>(null)
    val addDoStepOne: LiveData<CreateStepOneDeliveryOrderBody?> = _addDoStepOne

    private val _logistic = MutableLiveData<String?>(null)
    val logistic: LiveData<String?> = _logistic

    private val _kendaraan = MutableLiveData<String?>(null)
    val kendaraan: LiveData<String?> = _kendaraan

    private val _gudang = MutableLiveData<String?>(null)
    val gudang: LiveData<String?> = _gudang

    private val _perusahaan = MutableLiveData<AllPerusahaan?>(null)
    val perusahaan: LiveData<AllPerusahaan?> = _perusahaan

    fun setPerusahaan(perusahaan: AllPerusahaan) {
        _perusahaan.value = perusahaan
    }

    private val _messageResponse = MutableLiveData<Event<String?>>()
    val messageResponse: LiveData<Event<String?>> = _messageResponse

    fun setCanSwipe(canSwipe: Boolean) {
        _canSwipe.value = canSwipe
    }

    fun setAddDoStepOne(addDoStepOne: CreateStepOneDeliveryOrderBody) {
        _addDoStepOne.value = addDoStepOne
    }
}



