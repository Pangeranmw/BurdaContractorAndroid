package com.android.burdacontractor.feature.beranda.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.burdacontractor.core.data.Resource
import com.android.burdacontractor.core.domain.model.Event
import com.android.burdacontractor.core.domain.model.User
import com.android.burdacontractor.core.domain.model.enums.StateResponse
import com.android.burdacontractor.core.domain.model.enums.SuratJalanTipe
import com.android.burdacontractor.core.utils.LiveNetworkChecker
import com.android.burdacontractor.feature.deliveryorder.data.source.remote.response.DataAllDeliveryOrderWithCountItem
import com.android.burdacontractor.feature.deliveryorder.data.source.remote.response.DeliveryOrderItem
import com.android.burdacontractor.feature.deliveryorder.domain.model.AllDeliveryOrder
import com.android.burdacontractor.feature.deliveryorder.domain.model.DataAllDeliveryOrderWithCount
import com.android.burdacontractor.feature.deliveryorder.domain.usecase.GetAllDeliveryOrderDalamPerjalananByUserUseCase
import com.android.burdacontractor.feature.deliveryorder.domain.usecase.GetSomeActiveDeliveryOrderUseCase
import com.android.burdacontractor.feature.kendaraan.data.source.remote.response.KendaraanByLogisticItem
import com.android.burdacontractor.feature.kendaraan.domain.model.KendaraanByLogistic
import com.android.burdacontractor.feature.kendaraan.domain.usecase.GetKendaraanByLogisticUseCase
import com.android.burdacontractor.feature.profile.data.source.remote.response.UserByTokenItem
import com.android.burdacontractor.feature.profile.domain.usecase.GetUserByTokenUseCase
import com.android.burdacontractor.feature.suratjalan.data.source.remote.response.DataAllSuratJalanWithCountItem
import com.android.burdacontractor.feature.suratjalan.data.source.remote.response.SuratJalanItem
import com.android.burdacontractor.feature.suratjalan.domain.model.AllSuratJalan
import com.android.burdacontractor.feature.suratjalan.domain.model.DataAllSuratJalanWithCount
import com.android.burdacontractor.feature.suratjalan.domain.usecase.GetAllSuratJalanDalamPerjalananByUserUseCase
import com.android.burdacontractor.feature.suratjalan.domain.usecase.GetSomeActiveSuratJalanUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BerandaViewModel @Inject constructor(
    val liveNetworkChecker: LiveNetworkChecker,
    private val getUserByTokenUseCase: GetUserByTokenUseCase,
    private val getSomeActiveSuratJalanUseCase: GetSomeActiveSuratJalanUseCase,
    private val getKendaraanByLogisticUseCase: GetKendaraanByLogisticUseCase,
    private val getAllSuratJalanDalamPerjalananByUserUseCase: GetAllSuratJalanDalamPerjalananByUserUseCase,
    private val getSomeActiveDeliveryOrderUseCase: GetSomeActiveDeliveryOrderUseCase,
    private val getAllDeliveryOrderDalamPerjalananByUserUseCase: GetAllDeliveryOrderDalamPerjalananByUserUseCase
) : ViewModel() {

    private val _state = MutableLiveData<StateResponse?>()
    val state: LiveData<StateResponse?> = _state

    private val _user = MutableLiveData<UserByTokenItem>()
    val user: LiveData<UserByTokenItem> = _user

    private val _deliveryOrder = MutableLiveData<DataAllDeliveryOrderWithCountItem>()
    val deliveryOrder: LiveData<DataAllDeliveryOrderWithCountItem> = _deliveryOrder

    private val _sjPengirimanGp = MutableLiveData<DataAllSuratJalanWithCountItem>()
    val sjPengirimanGp: LiveData<DataAllSuratJalanWithCountItem> = _sjPengirimanGp

    private val _sjPengirimanPp = MutableLiveData<DataAllSuratJalanWithCountItem>()
    val sjPengirimanPp: LiveData<DataAllSuratJalanWithCountItem> = _sjPengirimanPp

    private val _sjPengembalian = MutableLiveData<DataAllSuratJalanWithCountItem>()
    val sjPengembalian: LiveData<DataAllSuratJalanWithCountItem> = _sjPengembalian

    private val _sjDalamPerjalanan = MutableLiveData<List<SuratJalanItem>>()
    val sjDalamPerjalanan: LiveData<List<SuratJalanItem>> = _sjDalamPerjalanan

    private val _doDalamPerjalanan = MutableLiveData<List<DeliveryOrderItem>>()
    val doDalamPerjalanan: LiveData<List<DeliveryOrderItem>> = _doDalamPerjalanan

    private val _kendaraanByLogistic = MutableLiveData<KendaraanByLogisticItem?>()
    val kendaraanByLogistic: LiveData<KendaraanByLogisticItem?> = _kendaraanByLogistic

    private val _messageResponse = MutableLiveData<Event<String?>>()
    val messageResponse : LiveData<Event<String?>> = _messageResponse

    init {
        getUserByToken()
        getSomeActiveDeliveryOrder()
        getAllDeliveryOrderDalamPerjalananByUser()
        getSomeActiveSuratJalan(SuratJalanTipe.PENGEMBALIAN)
        getSomeActiveSuratJalan(SuratJalanTipe.PENGIRIMAN_GUDANG_PROYEK)
        getSomeActiveSuratJalan(SuratJalanTipe.PENGIRIMAN_PROYEK_PROYEK)
        getAllSuratJalanDalamPerjalananByUser()
        getKendaraanByLogistic()
    }

    fun getUserByToken(){
        viewModelScope.launch {
            getUserByTokenUseCase.execute().collect{
                when(it){
                    is Resource.Loading -> _state.value = StateResponse.LOADING
                    is Resource.Success -> {
                        _state.value = StateResponse.SUCCESS
                        _user.value = it.data!!
                    }
                    is Resource.Error -> {
                        _state.value = StateResponse.ERROR
                    }
                }
            }
        }
    }
    fun getSomeActiveDeliveryOrder(){
        viewModelScope.launch {
            getSomeActiveDeliveryOrderUseCase.execute().collect{
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

    fun getAllDeliveryOrderDalamPerjalananByUser(){
        viewModelScope.launch {
            getAllDeliveryOrderDalamPerjalananByUserUseCase.execute().collect{
                when(it){
                    is Resource.Loading -> _state.value = StateResponse.LOADING
                    is Resource.Success -> {
                        _state.value = StateResponse.SUCCESS
                        _doDalamPerjalanan.value = it.data!!
                    }
                    is Resource.Error -> {
                        _state.value = StateResponse.ERROR
                    }
                }
            }
        }
    }

    fun getSomeActiveSuratJalan(tipe: SuratJalanTipe){
        viewModelScope.launch {
            getSomeActiveSuratJalanUseCase.execute(tipe).collect{
                when(it){
                    is Resource.Loading -> _state.value = StateResponse.LOADING
                    is Resource.Success -> {
                        _state.value = StateResponse.SUCCESS
                        when (tipe) {
                            SuratJalanTipe.PENGIRIMAN_GUDANG_PROYEK -> {
                                _sjPengirimanGp.value = it.data!!
                            }
                            SuratJalanTipe.PENGIRIMAN_PROYEK_PROYEK -> {
                                _sjPengirimanPp.value = it.data!!
                            }
                            SuratJalanTipe.PENGEMBALIAN -> {
                                _sjPengembalian.value = it.data!!
                            }
                        }
                    }
                    is Resource.Error -> {
                        _state.value = StateResponse.ERROR
                    }
                }
            }
        }
    }

    fun getAllSuratJalanDalamPerjalananByUser(){
        viewModelScope.launch {
            getAllSuratJalanDalamPerjalananByUserUseCase.execute().collect{
                when(it){
                    is Resource.Loading -> _state.value = StateResponse.LOADING
                    is Resource.Success -> {
                        _state.value = StateResponse.SUCCESS
                        _sjDalamPerjalanan.value = it.data!!
                    }
                    is Resource.Error -> {
                        _state.value = StateResponse.ERROR
                    }
                }
            }
        }
    }

    fun getKendaraanByLogistic(){
        viewModelScope.launch {
            getKendaraanByLogisticUseCase.execute().collect{
                when(it){
                    is Resource.Loading -> _state.value = StateResponse.LOADING
                    is Resource.Success -> {
                        _state.value = StateResponse.SUCCESS
                        _kendaraanByLogistic.value = it.data!!
                    }
                    is Resource.Error -> {
                        _state.value = StateResponse.ERROR
                        _kendaraanByLogistic.value = it.data
                    }
                }
            }
        }
    }

}



