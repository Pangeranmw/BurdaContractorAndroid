package com.android.burdacontractor.feature.perusahaan.presentation.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import com.android.burdacontractor.core.data.Resource
import com.android.burdacontractor.core.domain.model.Event
import com.android.burdacontractor.core.domain.model.enums.StateResponse
import com.android.burdacontractor.core.utils.LiveNetworkChecker
import com.android.burdacontractor.feature.deliveryorder.domain.model.AllDeliveryOrder
import com.android.burdacontractor.feature.perusahaan.domain.model.PerusahaanById
import com.android.burdacontractor.feature.perusahaan.domain.usecase.DeletePerusahaanUseCase
import com.android.burdacontractor.feature.perusahaan.domain.usecase.GetActiveDeliveryOrderByPerusahaanUseCase
import com.android.burdacontractor.feature.perusahaan.domain.usecase.GetPerusahaanByIdUseCase
import com.android.burdacontractor.feature.perusahaan.domain.usecase.GetStatistikDeliveryOrderByPerusahaanUseCase
import com.android.burdacontractor.feature.profile.data.source.remote.response.UserByTokenItem
import com.android.burdacontractor.feature.profile.domain.usecase.GetUserByTokenUseCase
import com.android.burdacontractor.feature.suratjalan.data.source.remote.response.StatisticCountTitleItem
import com.android.burdacontractor.feature.suratjalan.domain.model.AllSuratJalan
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PerusahaanDetailViewModel @Inject constructor(
    val liveNetworkChecker: LiveNetworkChecker,
    private val getPerusahaanByIdUseCase: GetPerusahaanByIdUseCase,
    private val getUserByTokenUseCase: GetUserByTokenUseCase,
    private val deletePerusahaanUseCase: DeletePerusahaanUseCase,
    private val getActiveDeliveryOrderByPerusahaanUseCase: GetActiveDeliveryOrderByPerusahaanUseCase,
    private val getStatistikDeliveryOrderByPerusahaanUseCase: GetStatistikDeliveryOrderByPerusahaanUseCase,
) : ViewModel() {

    private val _state = MutableLiveData<StateResponse?>()
    val state: LiveData<StateResponse?> = _state

    private val _user = MutableLiveData<UserByTokenItem>()
    val user: LiveData<UserByTokenItem> = _user

    private val _id = MutableLiveData<String?>(null)
    val id: LiveData<String?> = _id

    private val _messageResponse = MutableLiveData<Event<String?>>()
    val messageResponse: LiveData<Event<String?>> = _messageResponse

    private val _perusahaan = MutableLiveData<PerusahaanById>()
    val perusahaan: LiveData<PerusahaanById> = _perusahaan

    private val _deliveryOrder = MutableLiveData<List<AllDeliveryOrder>>()
    val deliveryOrder: LiveData<List<AllDeliveryOrder>> = _deliveryOrder

    private val _statDeliveryOrder = MutableLiveData<List<StatisticCountTitleItem>>()
    val statDeliveryOrder: LiveData<List<StatisticCountTitleItem>> = _statDeliveryOrder

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
                    getPerusahaanById(id)
                    getActiveDeliveryOrder(id)
                    getStatistikDeliveryOrder(id)
//                    getActiveSuratJalan(id, SuratJalanTipe.PENGIRIMAN_PROYEK_PROYEK)
//                    getActiveSuratJalan(id, SuratJalanTipe.PENGIRIMAN_GUDANG_PROYEK)
//                    getActiveSuratJalan(id, SuratJalanTipe.PENGEMBALIAN)
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

    fun deletePerusahaan(id: String, listener: () -> Unit) {
        viewModelScope.launch {
            deletePerusahaanUseCase.execute(id).collect {
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
            getActiveDeliveryOrderByPerusahaanUseCase.execute(id).collect { res ->
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

    fun getStatistikDeliveryOrder(id: String) {
        viewModelScope.launch {
            getStatistikDeliveryOrderByPerusahaanUseCase.execute(id).collect { res ->
                when (res) {
                    is Resource.Loading -> _state.value = StateResponse.LOADING
                    is Resource.Success -> {
                        _state.value = StateResponse.SUCCESS
                        _statDeliveryOrder.value = res.data!!
                    }

                    is Resource.Error -> {
                        _state.value = StateResponse.ERROR
                        _messageResponse.value = Event(res.message)
                    }
                }
            }
        }
    }

    //    fun getActiveSuratJalan(id: String, tipe: SuratJalanTipe) {
//        viewModelScope.launch {
//            getActiveSuratJalanByPerusahaanUseCase.execute(id, tipe).collect { res ->
//                when (res) {
//                    is Resource.Loading -> _state.value = StateResponse.LOADING
//                    is Resource.Success -> {
//                        _state.value = StateResponse.SUCCESS
//                        when (tipe) {
//                            SuratJalanTipe.PENGEMBALIAN -> _sjPengembalian.value = res.data!!
//                            SuratJalanTipe.PENGIRIMAN_GUDANG_PROYEK -> _sjPengirimanGp.value =
//                                res.data!!
//
//                            SuratJalanTipe.PENGIRIMAN_PROYEK_PROYEK -> _sjPengirimanPp.value =
//                                res.data!!
//                        }
//                    }
//
//                    is Resource.Error -> {
//                        _state.value = StateResponse.ERROR
//                        _messageResponse.value = Event(res.message)
//                    }
//                }
//            }
//        }
//    }
    fun getPerusahaanById(id: String) {
        viewModelScope.launch {
            getPerusahaanByIdUseCase.execute(id).collect { res ->
                when (res) {
                    is Resource.Loading -> _state.value = StateResponse.LOADING
                    is Resource.Success -> {
                        _state.value = StateResponse.SUCCESS
                        _perusahaan.value = res.data!!
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



