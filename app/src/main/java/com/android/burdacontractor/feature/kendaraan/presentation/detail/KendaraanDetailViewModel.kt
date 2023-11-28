package com.android.burdacontractor.feature.kendaraan.presentation.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import com.android.burdacontractor.core.data.Resource
import com.android.burdacontractor.core.domain.model.Event
import com.android.burdacontractor.core.domain.model.enums.StateResponse
import com.android.burdacontractor.core.domain.model.enums.SuratJalanTipe
import com.android.burdacontractor.core.utils.LiveNetworkChecker
import com.android.burdacontractor.feature.deliveryorder.domain.model.AllDeliveryOrder
import com.android.burdacontractor.feature.gudang.domain.model.GudangById
import com.android.burdacontractor.feature.gudang.domain.usecase.GetGudangByIdUseCase
import com.android.burdacontractor.feature.kendaraan.domain.model.Kendaraan
import com.android.burdacontractor.feature.kendaraan.domain.usecase.CancelReturnKendaraanUseCase
import com.android.burdacontractor.feature.kendaraan.domain.usecase.DeleteKendaraanUseCase
import com.android.burdacontractor.feature.kendaraan.domain.usecase.DeletePengendaraUseCase
import com.android.burdacontractor.feature.kendaraan.domain.usecase.GetActiveDeliveryOrderByKendaraanUseCase
import com.android.burdacontractor.feature.kendaraan.domain.usecase.GetActiveSuratJalanByKendaraanUseCase
import com.android.burdacontractor.feature.kendaraan.domain.usecase.GetKendaraanByIdUseCase
import com.android.burdacontractor.feature.kendaraan.domain.usecase.ReturnKendaraanUseCase
import com.android.burdacontractor.feature.profile.data.source.remote.response.UserByTokenItem
import com.android.burdacontractor.feature.profile.domain.usecase.GetUserByTokenUseCase
import com.android.burdacontractor.feature.proyek.domain.model.LogisticById
import com.android.burdacontractor.feature.proyek.domain.usecase.GetLogisticByIdUseCase
import com.android.burdacontractor.feature.suratjalan.domain.model.AllSuratJalan
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class KendaraanDetailViewModel @Inject constructor(
    val liveNetworkChecker: LiveNetworkChecker,
    private val getKendaraanByIdUseCase: GetKendaraanByIdUseCase,
    private val getUserByTokenUseCase: GetUserByTokenUseCase,
    private val deleteKendaraanUseCase: DeleteKendaraanUseCase,
    private val deletePengendaraUseCase: DeletePengendaraUseCase,
    private val getGudangByIdUseCase: GetGudangByIdUseCase,
    private val getLogisticByIdUseCase: GetLogisticByIdUseCase,
    private val getActiveDeliveryOrderByKendaraanUseCase: GetActiveDeliveryOrderByKendaraanUseCase,
    private val getActiveSuratJalanByKendaraanUseCase: GetActiveSuratJalanByKendaraanUseCase,
    private val returnKendaraanUseCase: ReturnKendaraanUseCase,
    private val cancelReturnKendaraanUseCase: CancelReturnKendaraanUseCase,
) : ViewModel() {

    private val _state = MutableLiveData<StateResponse?>()
    val state: LiveData<StateResponse?> = _state

    private val _user = MutableLiveData<UserByTokenItem>()
    val user: LiveData<UserByTokenItem> = _user

    private val _id = MutableLiveData<String?>(null)
    val id: LiveData<String?> = _id

    private val _messageResponse = MutableLiveData<Event<String?>>()
    val messageResponse: LiveData<Event<String?>> = _messageResponse

    private val _kendaraan = MutableLiveData<Kendaraan>()
    val kendaraan: LiveData<Kendaraan> = _kendaraan

    private val _gudang = MutableLiveData<GudangById>()
    val gudang: LiveData<GudangById> = _gudang

    private val _logistic = MutableLiveData<LogisticById>()
    val logistic: LiveData<LogisticById> = _logistic

    private val _deliveryOrder = MutableLiveData<List<AllDeliveryOrder>>()
    val deliveryOrder: LiveData<List<AllDeliveryOrder>> = _deliveryOrder

    private val _sjPengirimanGp = MutableLiveData<List<AllSuratJalan>>()
    val sjPengirimanGp: LiveData<List<AllSuratJalan>> = _sjPengirimanGp

    private val _sjPengirimanPp = MutableLiveData<List<AllSuratJalan>>()
    val sjPengirimanPp: LiveData<List<AllSuratJalan>> = _sjPengirimanPp

    private val _sjPengembalian = MutableLiveData<List<AllSuratJalan>>()
    val sjPengembalian: LiveData<List<AllSuratJalan>> = _sjPengembalian

    init {
        getUserByToken()
        viewModelScope.launch {
            _id.asFlow().collect {
                it?.let { id ->
                    getKendaraanById(id)
                    getActiveDeliveryOrder(id)
                    getActiveSuratJalan(id, SuratJalanTipe.PENGIRIMAN_PROYEK_PROYEK)
                    getActiveSuratJalan(id, SuratJalanTipe.PENGIRIMAN_GUDANG_PROYEK)
                    getActiveSuratJalan(id, SuratJalanTipe.PENGEMBALIAN)
                }
            }
        }
    }

    fun setId(id: String) {
        _id.value = id
    }

    private fun getUserByToken() {
        viewModelScope.launch {
            getUserByTokenUseCase.execute().collect {
                when (it) {
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

    fun deleteKendaraan(id: String, listener: () -> Unit) {
        viewModelScope.launch {
            deleteKendaraanUseCase.execute(id).collect {
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

    fun returnKendaraan(id: String, listener: () -> Unit) {
        viewModelScope.launch {
            returnKendaraanUseCase.execute(id).collect {
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

    fun cancelReturnKendaraan(id: String, listener: () -> Unit) {
        viewModelScope.launch {
            cancelReturnKendaraanUseCase.execute(id).collect {
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

    fun deletePengendara(id: String, listener: () -> Unit) {
        viewModelScope.launch {
            deletePengendaraUseCase.execute(id).collect {
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

    fun getActiveDeliveryOrder(id: String) {
        viewModelScope.launch {
            getActiveDeliveryOrderByKendaraanUseCase.execute(id).collect { res ->
                when (res) {
                    is Resource.Loading -> _state.value = StateResponse.LOADING
                    is Resource.Success -> {
                        _state.value = StateResponse.SUCCESS
                        _deliveryOrder.value = res.data!!
                    }

                    is Resource.Error -> {
                        _state.value = StateResponse.ERROR
                        _messageResponse.value = Event(res.message)
                    }
                }
            }
        }
    }

    fun getActiveSuratJalan(id: String, tipe: SuratJalanTipe) {
        viewModelScope.launch {
            getActiveSuratJalanByKendaraanUseCase.execute(id, tipe).collect { res ->
                when (res) {
                    is Resource.Loading -> _state.value = StateResponse.LOADING
                    is Resource.Success -> {
                        _state.value = StateResponse.SUCCESS
                        when (tipe) {
                            SuratJalanTipe.PENGEMBALIAN -> _sjPengembalian.value = res.data!!
                            SuratJalanTipe.PENGIRIMAN_GUDANG_PROYEK -> _sjPengirimanGp.value =
                                res.data!!

                            SuratJalanTipe.PENGIRIMAN_PROYEK_PROYEK -> _sjPengirimanPp.value =
                                res.data!!
                        }
                    }

                    is Resource.Error -> {
                        _state.value = StateResponse.ERROR
                        _messageResponse.value = Event(res.message)
                    }
                }
            }
        }
    }

    fun getLogisticById(id: String) {
        viewModelScope.launch {
            getLogisticByIdUseCase.execute(id).collect { res ->
                when (res) {
                    is Resource.Loading -> _state.value = StateResponse.LOADING
                    is Resource.Success -> {
                        _state.value = StateResponse.SUCCESS
                        _logistic.value = res.data!!
                    }

                    is Resource.Error -> {
                        _state.value = StateResponse.ERROR
                        _messageResponse.value = Event(res.message)
                    }
                }
            }
        }
    }

    fun getGudangById(id: String) {
        viewModelScope.launch {
            getGudangByIdUseCase.execute(id).collect { res ->
                when (res) {
                    is Resource.Loading -> _state.value = StateResponse.LOADING
                    is Resource.Success -> {
                        _state.value = StateResponse.SUCCESS
                        _gudang.value = res.data!!
                    }

                    is Resource.Error -> {
                        _state.value = StateResponse.ERROR
                        _messageResponse.value = Event(res.message)
                    }
                }
            }
        }
    }

    fun getKendaraanById(id: String) {
        viewModelScope.launch {
            getKendaraanByIdUseCase.execute(id).collect { res ->
                when (res) {
                    is Resource.Loading -> _state.value = StateResponse.LOADING
                    is Resource.Success -> {
                        _state.value = StateResponse.SUCCESS
                        _kendaraan.value = res.data!!
                        res.data.logisticId?.let {
                            getLogisticById(it)
                        }
                        getGudangById(res.data.gudangId)
                    }

                    is Resource.Error -> {
                        _state.value = StateResponse.ERROR
                        _messageResponse.value = Event(res.message)
                    }
                }
            }
        }
    }
}



