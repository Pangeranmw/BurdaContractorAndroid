package com.android.burdacontractor.feature.beranda.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import com.android.burdacontractor.core.data.Resource
import com.android.burdacontractor.core.domain.model.Event
import com.android.burdacontractor.core.domain.model.enums.StateResponse
import com.android.burdacontractor.core.domain.model.enums.SuratJalanTipe
import com.android.burdacontractor.core.domain.model.enums.UserRole
import com.android.burdacontractor.core.utils.LiveNetworkChecker
import com.android.burdacontractor.feature.deliveryorder.data.source.remote.response.DataAllDeliveryOrderWithCountItem
import com.android.burdacontractor.feature.deliveryorder.domain.model.AllDeliveryOrder
import com.android.burdacontractor.feature.deliveryorder.domain.usecase.GetAllDeliveryOrderDalamPerjalananByUserUseCase
import com.android.burdacontractor.feature.deliveryorder.domain.usecase.GetSomeActiveDeliveryOrderUseCase
import com.android.burdacontractor.feature.kendaraan.domain.model.KendaraanByLogistic
import com.android.burdacontractor.feature.kendaraan.domain.usecase.GetKendaraanByLogisticUseCase
import com.android.burdacontractor.feature.profile.data.source.remote.response.UserByTokenItem
import com.android.burdacontractor.feature.profile.domain.usecase.GetUserByTokenUseCase
import com.android.burdacontractor.feature.suratjalan.data.source.remote.response.DataAllSuratJalanWithCountItem
import com.android.burdacontractor.feature.suratjalan.data.source.remote.response.StatisticCountTitleItem
import com.android.burdacontractor.feature.suratjalan.data.source.remote.response.SuratJalanItem
import com.android.burdacontractor.feature.suratjalan.domain.usecase.GetAllSuratJalanDalamPerjalananByUserUseCase
import com.android.burdacontractor.feature.suratjalan.domain.usecase.GetSomeActiveSuratJalanUseCase
import com.android.burdacontractor.feature.suratjalan.domain.usecase.GetStatistikMenungguSuratJalanUseCase
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
    private val getAllDeliveryOrderDalamPerjalananByUserUseCase: GetAllDeliveryOrderDalamPerjalananByUserUseCase,
    private val getStatistikMenungguSuratJalanUseCase: GetStatistikMenungguSuratJalanUseCase
) : ViewModel() {

    private val _state = MutableLiveData(StateResponse.LOADING)
    val state: LiveData<StateResponse> = _state

    private val _role = MutableLiveData<String>(null)
    val role: LiveData<String?> = _role

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

    private val _doDalamPerjalanan = MutableLiveData<List<AllDeliveryOrder>>()
    val doDalamPerjalanan: LiveData<List<AllDeliveryOrder>> = _doDalamPerjalanan

    private val _statistikMenungguSuratJalan = MutableLiveData<List<StatisticCountTitleItem>>()
    val statistikMenungguSuratJalan: LiveData<List<StatisticCountTitleItem>> =
        _statistikMenungguSuratJalan

    private val _kendaraanByLogistic = MutableLiveData<KendaraanByLogistic?>()
    val kendaraanByLogistic: LiveData<KendaraanByLogistic?> = _kendaraanByLogistic

    private val _messageResponse = MutableLiveData<Event<String?>>()
    val messageResponse: LiveData<Event<String?>> = _messageResponse

    init {
        getUserByToken()
        getAllData()
    }

    fun getAllData() {
        viewModelScope.launch {
            role.asFlow().collect {
                it?.let { role ->
                    if (role == UserRole.ADMIN_GUDANG.name || role == UserRole.ADMIN.name) {
                        getStatistikMenungguSuratJalan()
                    }
                    if (role == UserRole.LOGISTIC.name) {
                        getKendaraanByLogistic()
                    }
                    if (role == UserRole.LOGISTIC.name ||
                        role == UserRole.ADMIN_GUDANG.name ||
                        role == UserRole.ADMIN.name ||
                        role == UserRole.SUPERVISOR.name ||
                        role == UserRole.PROJECT_MANAGER.name ||
                        role == UserRole.SITE_MANAGER.name
                    ) {
                        getSomeActiveSuratJalan(SuratJalanTipe.PENGEMBALIAN)
                        getSomeActiveSuratJalan(SuratJalanTipe.PENGIRIMAN_PROYEK_PROYEK)
                        getSomeActiveSuratJalan(SuratJalanTipe.PENGIRIMAN_GUDANG_PROYEK)
                        getAllSuratJalanDalamPerjalananByUser()
                    }
                    if(role == UserRole.LOGISTIC.name ||
                        role == UserRole.ADMIN_GUDANG.name ||
                        role == UserRole.ADMIN.name ||
                        role == UserRole.PURCHASING.name
                    ) {
                        getSomeActiveDeliveryOrder()
                        getAllDeliveryOrderDalamPerjalananByUser()
                    }
                }
            }
        }
    }
    fun setRole(role: String){
        _role.value = role
    }
    fun getUserByToken(){
        viewModelScope.launch {
            getUserByTokenUseCase.execute().collect{
                when(it){
                    is Resource.Loading -> _state.value = StateResponse.LOADING
                    is Resource.Success -> {
                        _state.value = StateResponse.SUCCESS
                        _user.value = it.data!!
                        _messageResponse.value = Event("Berhasil Mendapatkan Data User")
                    }
                    is Resource.Error -> {
                        _messageResponse.value = Event(it.message)
                        _state.value = StateResponse.ERROR
                    }
                }
            }
        }
    }
    private fun getStatistikMenungguSuratJalan(){
        viewModelScope.launch {
            getStatistikMenungguSuratJalanUseCase.execute().collect{
                when(it){
                    is Resource.Loading -> _state.value = StateResponse.LOADING
                    is Resource.Success -> {
                        _state.value = StateResponse.SUCCESS
                        _statistikMenungguSuratJalan.value = it.data!!
                        _messageResponse.value = Event("Berhasil Mendapatkan Data Menunggu Surat Jalan")
                    }
                    is Resource.Error -> {
                        _state.value = StateResponse.ERROR
                        _messageResponse.value = Event(it.message)
                    }
                }
            }
        }
    }
    private fun getSomeActiveDeliveryOrder(){
        viewModelScope.launch {
            getSomeActiveDeliveryOrderUseCase.execute().collect{
                when(it){
                    is Resource.Loading -> _state.value = StateResponse.LOADING
                    is Resource.Success -> {
                        _state.value = StateResponse.SUCCESS
                        _deliveryOrder.value = it.data!!
                        _messageResponse.value = Event("Berhasil Mendapatkan Delivery Order Aktif")
                    }
                    is Resource.Error -> {
                        _state.value = StateResponse.ERROR
                        _messageResponse.value = Event(it.message)
                    }
                }
            }
        }
    }

    private fun getAllDeliveryOrderDalamPerjalananByUser(){
        viewModelScope.launch {
            getAllDeliveryOrderDalamPerjalananByUserUseCase.execute().collect{
                when(it){
                    is Resource.Loading -> _state.value = StateResponse.LOADING
                    is Resource.Success -> {
                        _state.value = StateResponse.SUCCESS
                        _doDalamPerjalanan.value = it.data!!
                        _messageResponse.value = Event("Berhasil Mendapatkan Delivery Order Dalam Perjalanan")
                    }
                    is Resource.Error -> {
                        _state.value = StateResponse.ERROR
                        _messageResponse.value = Event(it.message)
                    }
                }
            }
        }
    }

    private fun getSomeActiveSuratJalan(tipe: SuratJalanTipe){
        viewModelScope.launch {
            getSomeActiveSuratJalanUseCase.execute(tipe).collect{
                when(it){
                    is Resource.Loading -> _state.value = StateResponse.LOADING
                    is Resource.Success -> {
                        _state.value = StateResponse.SUCCESS
                        when (tipe) {
                            SuratJalanTipe.PENGIRIMAN_GUDANG_PROYEK -> {
                                _sjPengirimanGp.value = it.data!!
                                _messageResponse.value = Event("Berhasil Mendapatkan Surat Jalan Pengiriman Gudang Proyek Aktif")
                            }
                            SuratJalanTipe.PENGIRIMAN_PROYEK_PROYEK -> {
                                _sjPengirimanPp.value = it.data!!
                                _messageResponse.value = Event("Berhasil Mendapatkan Surat Jalan Pengiriman Proyek Proyek Aktif")
                            }
                            SuratJalanTipe.PENGEMBALIAN -> {
                                _sjPengembalian.value = it.data!!
                                _messageResponse.value = Event("Berhasil Mendapatkan Surat Jalan Pengembalian Aktif")
                            }
                        }
                    }
                    is Resource.Error -> {
                        _state.value = StateResponse.ERROR
                        _messageResponse.value = Event(it.message)
                    }
                }
            }
        }
    }

    private fun getAllSuratJalanDalamPerjalananByUser(){
        viewModelScope.launch {
            getAllSuratJalanDalamPerjalananByUserUseCase.execute().collect{
                when(it){
                    is Resource.Loading -> _state.value = StateResponse.LOADING
                    is Resource.Success -> {
                        _state.value = StateResponse.SUCCESS
                        _sjDalamPerjalanan.value = it.data!!
                        _messageResponse.value = Event("Berhasil Mendapatkan Surat Jalan Dalam Perjalanan")
                    }
                    is Resource.Error -> {
                        _state.value = StateResponse.ERROR
                        _messageResponse.value = Event(it.message)
                    }
                }
            }
        }
    }

    private fun getKendaraanByLogistic(){
        viewModelScope.launch {
            getKendaraanByLogisticUseCase.execute().collect{
                when(it){
                    is Resource.Loading -> _state.value = StateResponse.LOADING
                    is Resource.Success -> {
                        _state.value = StateResponse.SUCCESS
                        _kendaraanByLogistic.value = it.data!!
                        _messageResponse.value = Event("Berhasil Mendapatkan Kendaraan")
                    }
                    is Resource.Error -> {
                        _state.value = StateResponse.ERROR
                        _messageResponse.value = Event(it.message)
                    }
                }
            }
        }
    }
}



