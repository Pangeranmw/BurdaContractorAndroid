package com.android.burdacontractor.feature.deliveryorder.presentation.update

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.burdacontractor.core.data.Resource
import com.android.burdacontractor.core.domain.model.Event
import com.android.burdacontractor.core.domain.model.enums.StateResponse
import com.android.burdacontractor.core.utils.LiveNetworkChecker
import com.android.burdacontractor.feature.deliveryorder.data.source.remote.request.AddUpdateDeliveryOrderStepOneBody
import com.android.burdacontractor.feature.deliveryorder.data.source.remote.request.AddUpdateDeliveryOrderStepTwoBody
import com.android.burdacontractor.feature.deliveryorder.domain.model.PreOrder
import com.android.burdacontractor.feature.deliveryorder.domain.usecase.DeletePreOrderUseCase
import com.android.burdacontractor.feature.deliveryorder.domain.usecase.UpdateDeliveryOrderStepOneUseCase
import com.android.burdacontractor.feature.deliveryorder.domain.usecase.UpdateDeliveryOrderStepTwoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class UpdateDeliveryOrderViewModel @Inject constructor(
    val liveNetworkChecker: LiveNetworkChecker,
    private val updateDeliveryOrderStepOneUseCase: UpdateDeliveryOrderStepOneUseCase,
    private val updateDeliveryOrderStepTwoUseCase: UpdateDeliveryOrderStepTwoUseCase,
    private val deletePreOrderUseCase: DeletePreOrderUseCase,
) : ViewModel() {
    private val _state = MutableLiveData<StateResponse?>()
    val state: LiveData<StateResponse?> = _state

    private val _canSwipe = MutableLiveData(false)
    val canSwipe: LiveData<Boolean> = _canSwipe

    private val _untukPerhatian = MutableLiveData<String?>(null)
    val untukPerhatian: LiveData<String?> = _untukPerhatian

    private val _deliveryOrderId = MutableLiveData<String>()
    val deliveryOrderId: LiveData<String> = _deliveryOrderId

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

    fun updateCreateStepOneDo(
        logisticId: String,
        purchasingId: String,
        perusahaanId: String,
        gudangId: String,
        kendaraanId: String,
        perihal: String,
        tglPengambilan: String,
        untukPerhatian: String,
    ) {
        val data = AddUpdateDeliveryOrderStepOneBody(
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
            updateDeliveryOrderStepOneUseCase.execute(deliveryOrderId.value!!, data).collect {
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

    fun updateCreateStepTwoDo(
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
        val data = AddUpdateDeliveryOrderStepTwoBody(
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
            updateDeliveryOrderStepTwoUseCase.execute(deliveryOrderId.value!!, data).collect {
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
            kodePo = "",
            ukuran = ukuran,
            keterangan = keterangan,
            jumlah = jumlah,
            namaMaterial = namaMaterial,
            satuan = satuan,
            deliveryOrderId = "",
        )
        _listPreOrder.value!!.add(data)
    }

    fun removePreOrder(preOrderBody: PreOrder, successListener: () -> Unit) {
        val createdPo = _listPreOrder.value!!.count { !it.deliveryOrderId.isNullOrBlank() }
        if (preOrderBody.deliveryOrderId.isNullOrBlank()) {
            _listPreOrder.value!!.remove(preOrderBody)
            successListener()
        } else {
            if (createdPo > 1) {
                _listPreOrder.value!!.remove(preOrderBody)
                viewModelScope.launch {
                    deletePreOrderUseCase.execute(preOrderBody.id.toString()).collect {
                        when (it) {
                            is Resource.Loading -> _state.value = StateResponse.LOADING
                            is Resource.Success -> {
                                _state.value = StateResponse.SUCCESS
                                _messageResponse.value = Event(it.data?.message)
                                successListener()
                            }

                            is Resource.Error -> {
                                _state.value = StateResponse.ERROR
                                _messageResponse.value = Event(it.message)
                            }
                        }
                    }
                }
            } else {
                _messageResponse.value =
                    Event("Gagal Menghapus Pre Order, Minimal 1 PO Yang Telah Terbuat")
            }
        }
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

    fun setDeliveryOrderId(deliveryOrderId: String) {
        _deliveryOrderId.value = deliveryOrderId
    }

    fun setListPreOrder(listPreOrder: List<PreOrder>) {
        _listPreOrder.value = listPreOrder.toMutableList()
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



