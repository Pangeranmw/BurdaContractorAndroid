package com.android.burdacontractor.feature.deliveryorder.presentation.create

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.burdacontractor.core.data.Resource
import com.android.burdacontractor.core.domain.model.Event
import com.android.burdacontractor.core.domain.model.enums.StateResponse
import com.android.burdacontractor.core.utils.LiveNetworkChecker
import com.android.burdacontractor.feature.deliveryorder.data.source.remote.request.AddDeliveryOrderStepOneBody
import com.android.burdacontractor.feature.deliveryorder.data.source.remote.request.AddDeliveryOrderStepTwoBody
import com.android.burdacontractor.feature.deliveryorder.domain.model.PreOrder
import com.android.burdacontractor.feature.deliveryorder.domain.usecase.AddDeliveryOrderStepOneUseCase
import com.android.burdacontractor.feature.deliveryorder.domain.usecase.AddDeliveryOrderStepTwoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class AddDeliveryOrderViewModel @Inject constructor(
    val liveNetworkChecker: LiveNetworkChecker,
    val addDeliveryOrderStepOneUseCase: AddDeliveryOrderStepOneUseCase,
    val addDeliveryOrderStepTwoUseCase: AddDeliveryOrderStepTwoUseCase,
) : ViewModel() {
    private val _state = MutableLiveData<StateResponse?>()
    val state: LiveData<StateResponse?> = _state

    private val _canSwipe = MutableLiveData(false)
    val canSwipe: LiveData<Boolean> = _canSwipe

    private val _createStepOneDoBody = MutableLiveData<AddDeliveryOrderStepOneBody>()
    val createStepOneDoBody: LiveData<AddDeliveryOrderStepOneBody> = _createStepOneDoBody

    private val _untukPerhatian = MutableLiveData<String?>(null)
    val untukPerhatian: LiveData<String?> = _untukPerhatian

    private val _perihal = MutableLiveData("Delivery Order")
    val perihal: LiveData<String?> = _perihal

    private val _tglPengambilan = MutableLiveData<String?>(null)
    val tglPengambilan: LiveData<String?> = _tglPengambilan

    private val _kodeDo = MutableLiveData<String?>()
    val kodeDo: LiveData<String?> = _kodeDo

    private val _listPreOrder = MutableLiveData<MutableList<PreOrder>>(mutableListOf())
    val listPreOrder: LiveData<MutableList<PreOrder>> = _listPreOrder

    private val _messageResponse = MutableLiveData<Event<String?>>()
    val messageResponse: LiveData<Event<String?>> = _messageResponse

    fun addCreateStepOneDo(
        logisticId: String,
        purchasingId: String,
        perusahaanId: String,
        gudangId: String,
        kendaraanId: String,
        perihal: String,
        tglPengambilan: String,
        untukPerhatian: String,
    ) {
        val data = AddDeliveryOrderStepOneBody(
            logisticId = logisticId,
            purchasingId = purchasingId,
            perusahaanId = perusahaanId,
            gudangId = gudangId,
            kendaraanId = kendaraanId,
            perihal = perihal,
            tglPengambilan = tglPengambilan,
            untukPerhatian = untukPerhatian
        )
        viewModelScope.launch {
            addDeliveryOrderStepOneUseCase.execute(data).collect {
                when (it) {
                    is Resource.Loading -> _state.value = StateResponse.LOADING
                    is Resource.Success -> {
                        _state.value = StateResponse.SUCCESS
                        _kodeDo.value = it.data
                    }

                    is Resource.Error -> {
                        _state.value = StateResponse.ERROR
                    }
                }
            }
        }
    }

    fun addCreateStepTwoDo(
        logisticId: String,
        purchasingId: String,
        perusahaanId: String,
        gudangId: String,
        kendaraanId: String,
        perihal: String,
        tglPengambilan: String,
        untukPerhatian: String,
        listPreOrder: List<PreOrder>,
        successListener: (String) -> Unit?
    ) {
        val data = AddDeliveryOrderStepTwoBody(
            logisticId = logisticId,
            purchasingId = purchasingId,
            perusahaanId = perusahaanId,
            gudangId = gudangId,
            kendaraanId = kendaraanId,
            perihal = perihal,
            tglPengambilan = tglPengambilan,
            untukPerhatian = untukPerhatian,
            preOrder = listPreOrder,
        )
        viewModelScope.launch {
            addDeliveryOrderStepTwoUseCase.execute(data).collect {
                when (it) {
                    is Resource.Loading -> _state.value = StateResponse.LOADING
                    is Resource.Success -> {
                        _state.value = StateResponse.SUCCESS
                        successListener(it.data!!)
                    }

                    is Resource.Error -> {
                        _state.value = StateResponse.ERROR
                    }
                }
            }
        }
    }

    fun addPreOrder(
        ukuran: String,
        keterangan: String?,
        jumlah: Int,
        namaMaterial: String,
        satuan: String
    ) {
        val id = UUID.randomUUID().toString()
        val data = PreOrder(
            id = id,
            kodePo = id,
            ukuran = ukuran,
            keterangan = keterangan,
            jumlah = jumlah,
            namaMaterial = namaMaterial,
            satuan = satuan
        )
        _listPreOrder.value!!.add(data)
    }

    fun removePreOrder(preOrderBody: PreOrder) {
        _listPreOrder.value!!.remove(preOrderBody)
    }

    fun changePreOrder(
        id: String,
        ukuran: String,
        keterangan: String?,
        jumlah: Int,
        namaMaterial: String,
        satuan: String
    ) {
        _listPreOrder.value!!.find { it.id == id }?.ukuran = ukuran
        _listPreOrder.value!!.find { it.id == id }?.keterangan = keterangan
        _listPreOrder.value!!.find { it.id == id }?.jumlah = jumlah
        _listPreOrder.value!!.find { it.id == id }?.namaMaterial = namaMaterial
        _listPreOrder.value!!.find { it.id == id }?.satuan = satuan
    }

    fun setUntukPerhatian(untukPerhatian: String?) {
        _untukPerhatian.value = untukPerhatian
    }

    fun setPerihal(perihal: String?) {
        _perihal.value = perihal
    }

    fun setTglPengambilan(tglPengambilan: String?) {
        _tglPengambilan.value = tglPengambilan
    }

    fun setCanSwipe(canSwipe: Boolean) {
        _canSwipe.value = canSwipe
    }
}



