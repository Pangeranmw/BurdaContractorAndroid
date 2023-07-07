package com.android.burdacontractor.feature.deliveryorder.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.burdacontractor.core.domain.model.Event
import com.android.burdacontractor.feature.deliveryorder.domain.model.PreOrder
import com.android.burdacontractor.core.domain.model.enums.StateResponse
import com.android.burdacontractor.core.utils.LiveNetworkChecker
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DeliveryOrderViewModel @Inject constructor(val liveNetworkChecker: LiveNetworkChecker) : ViewModel() {
    private val _state = MutableLiveData<StateResponse?>()
    val state: LiveData<StateResponse?> = _state

    private val _listPreOrder = MutableLiveData<List<PreOrder>?>()
    val listPreOrder: LiveData<List<PreOrder>?> = _listPreOrder

    private val _preOrder = mutableListOf<PreOrder>()

    init {
        _listPreOrder.value = _preOrder
    }

    private val _messageResponse = MutableLiveData<Event<String?>>()
    val messageResponse : LiveData<Event<String?>> = _messageResponse

    fun addLocalPreOrder(namaMaterial: String, satuan: String, keterangan: String, jumlah: String){
        _preOrder.add(
            PreOrder(_preOrder.size.toString(), namaMaterial, satuan, keterangan, jumlah)
        )
        _listPreOrder.value = _preOrder
    }
    fun changeLocalPreOrder(id: String, namaMaterial: String, satuan: String, keterangan: String, jumlah: String){
        _preOrder.find { it.id == id }?.apply {
            this.namaMaterial = namaMaterial
            this.satuan = satuan
            this.keterangan = keterangan
            this.jumlah = jumlah
        }
        _listPreOrder.value = _preOrder
    }
    fun deleteLocalPreOrder(preOrder: PreOrder){
        _preOrder.remove(preOrder)
        _listPreOrder.value = _preOrder
    }
    fun deleteAllLocalPreOrder(preOrder: List<PreOrder>){
        _preOrder.removeAll(preOrder)
        _listPreOrder.value = _preOrder
    }
}



