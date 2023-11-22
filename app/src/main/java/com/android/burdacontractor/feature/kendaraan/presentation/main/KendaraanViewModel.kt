package com.android.burdacontractor.feature.kendaraan.presentation.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.android.burdacontractor.core.data.Resource
import com.android.burdacontractor.core.domain.model.Event
import com.android.burdacontractor.core.domain.model.enums.JenisKendaraan
import com.android.burdacontractor.core.domain.model.enums.StateResponse
import com.android.burdacontractor.core.domain.model.enums.StatusKendaraan
import com.android.burdacontractor.core.utils.LiveNetworkChecker
import com.android.burdacontractor.feature.gudang.domain.model.GudangById
import com.android.burdacontractor.feature.kendaraan.domain.model.AllKendaraan
import com.android.burdacontractor.feature.kendaraan.domain.usecase.GetAllKendaraanUseCase
import com.android.burdacontractor.feature.kendaraan.domain.usecase.GetKendaraanGudangUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class KendaraanViewModel @Inject constructor(
    val liveNetworkChecker: LiveNetworkChecker,
    private val getAllKendaraanUseCase: GetAllKendaraanUseCase,
    private val getKendaraanGudangUseCase: GetKendaraanGudangUseCase,
) : ViewModel() {
    private val _state = MutableLiveData<StateResponse?>()
    val state: LiveData<StateResponse?> = _state

    private val _coordinate = MutableLiveData<String?>(null)
    val coordinate: LiveData<String?> = _coordinate

    private val _gudangIndex = MutableLiveData<Int?>(null)
    val gudangIndex: LiveData<Int?> = _gudangIndex

    private val _listGudang = MutableLiveData<List<GudangById>>()
    val listGudang: LiveData<List<GudangById>> = _listGudang

    private val _jenisIndex = MutableLiveData<Int?>(null)
    val jenisIndex: LiveData<Int?> = _jenisIndex
    private val _listJenis = MutableLiveData(
        listOf(
            JenisKendaraan.MOTOR.name,
            JenisKendaraan.MINIBUS.name,
            JenisKendaraan.TRUCK.name,
            JenisKendaraan.MOBIL.name,
            JenisKendaraan.TRONTON.name,
            JenisKendaraan.PICKUP.name
        )
    )
    val listJenis: LiveData<List<String>> = _listJenis


    private val _statusIndex = MutableLiveData<Int?>(null)
    val statusIndex: LiveData<Int?> = _statusIndex

    private val _listStatus = MutableLiveData(
        listOf(
            StatusKendaraan.TERSEDIA.name,
            StatusKendaraan.DIGUNAKAN.name,
            StatusKendaraan.AJUKAN_PENGEMBALIAN.name,
        )
    )
    val listStatus: LiveData<List<String>> = _listStatus

    private val _search = MutableLiveData<String?>(null)
    val search: LiveData<String?> = _search

    private val _messageResponse = MutableLiveData<Event<String?>>()
    val messageResponse: LiveData<Event<String?>> = _messageResponse

    fun setGudangIndex(gudangIndex: Int?) {
        _gudangIndex.value = gudangIndex
    }

    fun setStatusIndex(statusIndex: Int?) {
        _statusIndex.value = statusIndex
    }

    fun setJenisIndex(jenisIndex: Int?) {
        _jenisIndex.value = jenisIndex
    }

    fun setState(state: StateResponse) {
        _state.value = state
    }

    fun setSearch(search: String) {
        _search.value = search
    }

    fun getKendaraanGudang() {
        viewModelScope.launch {
            getKendaraanGudangUseCase.execute().collect {
                when (it) {
                    is Resource.Loading -> _state.value = StateResponse.LOADING
                    is Resource.Success -> {
                        _listGudang.value = it.data!!
                    }

                    is Resource.Error -> {
                        _state.value = StateResponse.ERROR
                        _messageResponse.value = Event(it.message)
                    }
                }
            }
        }
    }

    fun getAllKendaraan(): LiveData<PagingData<AllKendaraan>> {
        var filterGudang: String? = null
        _gudangIndex.value?.let { index ->
            _listGudang.value?.let {
                filterGudang = it[index].id
            }
        }
        var filterJenis: String? = null
        _jenisIndex.value?.let { index ->
            _listJenis.value?.let {
                filterJenis = it[index]
            }
        }
        var filterStatus: String? = null
        _statusIndex.value?.let { index ->
            _listStatus.value?.let {
                filterStatus = it[index]
            }
        }
        return getAllKendaraanUseCase.execute(
            filter = filterJenis,
            gudang = filterGudang,
            status = filterStatus,
            search = _search.value,
        ).cachedIn(viewModelScope)
    }
}