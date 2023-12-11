package com.android.burdacontractor.feature.suratjalan.presentation.create

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.burdacontractor.core.data.Resource
import com.android.burdacontractor.core.domain.model.Event
import com.android.burdacontractor.core.domain.model.Proyek
import com.android.burdacontractor.core.domain.model.enums.StateResponse
import com.android.burdacontractor.core.domain.model.enums.SuratJalanTipe
import com.android.burdacontractor.core.utils.DataMapper.mapPeminjamanSuratJalanToAddUpdatePeminjamanPenggunaanSuratJalan
import com.android.burdacontractor.core.utils.DataMapper.mapPenggunaanSuratJalanToAddUpdatePeminjamanPenggunaanSuratJalan
import com.android.burdacontractor.core.utils.LiveNetworkChecker
import com.android.burdacontractor.feature.suratjalan.data.source.remote.request.AddUpdateSuratJalanBody
import com.android.burdacontractor.feature.suratjalan.domain.model.PeminjamanSuratJalan
import com.android.burdacontractor.feature.suratjalan.domain.model.PenggunaanSuratJalan
import com.android.burdacontractor.feature.suratjalan.domain.usecase.AddSuratJalanUseCase
import com.android.burdacontractor.feature.suratjalan.domain.usecase.GetAvailableProyekBySuratJalanTypeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddSuratJalanViewModel @Inject constructor(
    val liveNetworkChecker: LiveNetworkChecker,
    private val addSuratJalanUseCase: AddSuratJalanUseCase,
    private val getAvailableProyekBySuratJalanType: GetAvailableProyekBySuratJalanTypeUseCase,
) : ViewModel() {
    private val _state = MutableLiveData<StateResponse?>()
    val state: LiveData<StateResponse?> = _state

    private val _listTipe = MutableLiveData(
        listOf(
            "Pengiriman Gudang Proyek",
            "Pengiriman Proyek Proyek",
            "Pengembalian",
        )
    )
    val listTipe: LiveData<List<String>> = _listTipe

    private val _tipe = MutableLiveData<String?>(null)
    val tipe: LiveData<String?> = _tipe

    private val _listProyek = MutableLiveData<List<Proyek>>(emptyList())
    val listProyek: LiveData<List<Proyek>> = _listProyek

    private val _proyekIndex = MutableLiveData<Int?>(null)
    val proyekIndex: LiveData<Int?> = _proyekIndex

    private val _messageResponse = MutableLiveData<Event<String?>>()
    val messageResponse: LiveData<Event<String?>> = _messageResponse
    fun getAvailableProyekBySuratJalanType() {
        _tipe.value?.let { tipe ->
            viewModelScope.launch {
                getAvailableProyekBySuratJalanType.execute(tipe).collect {
                    when (it) {
                        is Resource.Loading -> _state.value = StateResponse.LOADING
                        is Resource.Success -> {
                            _state.value = StateResponse.SUCCESS
                            _listProyek.value = it.data!!
                        }

                        is Resource.Error -> {
                            _state.value = StateResponse.ERROR
                        }
                    }
                }
            }
        }
    }

    private fun convertToEnumType(tipe: String): String? {
        return when (tipe) {
            "Pengiriman Gudang Proyek" -> SuratJalanTipe.PENGIRIMAN_GUDANG_PROYEK.name
            "Pengiriman Proyek Proyek" -> SuratJalanTipe.PENGIRIMAN_PROYEK_PROYEK.name
            "Pengembalian" -> SuratJalanTipe.PENGEMBALIAN.name
            else -> null
        }
    }

    fun addSuratJalan(
        logisticId: String,
        kendaraanId: String,
        selectedListPeminjaman: MutableList<PeminjamanSuratJalan>,
        selectedListPenggunaan: MutableList<PenggunaanSuratJalan>,
        successListener: (String) -> Unit
    ) {
        val peminjaman = selectedListPeminjaman.toList()
        val penggunaan = selectedListPenggunaan.toList()
        if (peminjaman.isNotEmpty() || penggunaan.isNotEmpty()) {
            _tipe.value?.let { tipe ->
                _proyekIndex.value?.let { proyekIndex ->
                    _listProyek.value?.let { listProyek ->
                        val newPeminjaman =
                            mapPeminjamanSuratJalanToAddUpdatePeminjamanPenggunaanSuratJalan(
                                peminjaman
                            )
                        val newPenggunaan =
                            mapPenggunaanSuratJalanToAddUpdatePeminjamanPenggunaanSuratJalan(
                                penggunaan
                            )
                        val data = AddUpdateSuratJalanBody(
                            logisticId = logisticId,
                            kendaraanId = kendaraanId,
                            proyekId = listProyek[proyekIndex].id,
                            peminjaman = newPeminjaman,
                            penggunaan = newPenggunaan,
                            tipe = tipe
                        )
                        viewModelScope.launch {
                            addSuratJalanUseCase.execute(data).collect {
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
                }
            }
        }
    }

    fun setTipe(tipe: String) {
        _tipe.value = convertToEnumType(tipe)
    }

    fun setProyekIndex(proyekIndex: Int?) {
        _proyekIndex.value = proyekIndex
    }
}



