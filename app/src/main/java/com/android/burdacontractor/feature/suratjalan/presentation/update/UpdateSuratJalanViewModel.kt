package com.android.burdacontractor.feature.suratjalan.presentation.update

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.burdacontractor.core.data.Resource
import com.android.burdacontractor.core.domain.model.Event
import com.android.burdacontractor.core.domain.model.enums.StateResponse
import com.android.burdacontractor.core.utils.DataMapper.mapPeminjamanSuratJalanToAddUpdatePeminjamanPenggunaanSuratJalan
import com.android.burdacontractor.core.utils.DataMapper.mapPenggunaanSuratJalanToAddUpdatePeminjamanPenggunaanSuratJalan
import com.android.burdacontractor.core.utils.LiveNetworkChecker
import com.android.burdacontractor.feature.suratjalan.data.source.remote.request.AddUpdateSuratJalanBody
import com.android.burdacontractor.feature.suratjalan.domain.model.PeminjamanSuratJalan
import com.android.burdacontractor.feature.suratjalan.domain.model.PenggunaanSuratJalan
import com.android.burdacontractor.feature.suratjalan.domain.usecase.UpdateSuratJalanUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UpdateSuratJalanViewModel @Inject constructor(
    val liveNetworkChecker: LiveNetworkChecker,
    private val updateSuratJalanUseCase: UpdateSuratJalanUseCase,
) : ViewModel() {
    private val _state = MutableLiveData<StateResponse?>()
    val state: LiveData<StateResponse?> = _state

    private val _tipe = MutableLiveData<String?>(null)
    val tipe: LiveData<String?> = _tipe

    private val _proyekIndex = MutableLiveData<Int?>(null)
    val proyekIndex: LiveData<Int?> = _proyekIndex

    private val _messageResponse = MutableLiveData<Event<String?>>()
    val messageResponse: LiveData<Event<String?>> = _messageResponse

    fun updateSuratJalan(
        id: String,
        logisticId: String,
        kendaraanId: String,
        selectedListPeminjaman: MutableList<PeminjamanSuratJalan>,
        selectedListPenggunaan: MutableList<PenggunaanSuratJalan>,
        proyekId: String,
        successListener: (String) -> Unit
    ) {
        val peminjaman = selectedListPeminjaman.toList()
        val penggunaan = selectedListPenggunaan.toList()
        _tipe.value?.let { tipe ->
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
                proyekId = proyekId,
                peminjaman = newPeminjaman,
                penggunaan = newPenggunaan,
                tipe = tipe
            )
            viewModelScope.launch {
                updateSuratJalanUseCase.execute(id, data).collect {
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

    fun setTipe(tipe: String) {
        _tipe.value = tipe
    }
}



