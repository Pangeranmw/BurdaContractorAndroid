package com.android.burdacontractor.feature.suratjalan.presentation.create

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.burdacontractor.core.data.Resource
import com.android.burdacontractor.core.domain.model.Event
import com.android.burdacontractor.core.domain.model.enums.StateResponse
import com.android.burdacontractor.feature.suratjalan.domain.model.PeminjamanSuratJalan
import com.android.burdacontractor.feature.suratjalan.domain.usecase.GetAvailablePeminjamanByProyekUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PilihPeminjamanSuratJalanViewModel @Inject constructor(
    private val getAvailablePeminjamanByProyekUseCase: GetAvailablePeminjamanByProyekUseCase,
) : ViewModel() {
    private val _state = MutableLiveData<StateResponse?>()
    val state: LiveData<StateResponse?> = _state

    private val _tipe = MutableLiveData<String?>()
    private val _proyekId = MutableLiveData<String?>()

    fun setProyekId(proyekId: String) {
        _proyekId.value = proyekId
    }

    fun setTipe(tipe: String) {
        _tipe.value = tipe
    }

    private val _selectedListPeminjaman =
        MutableLiveData<MutableList<PeminjamanSuratJalan>>(mutableListOf())
    val selectedListPeminjaman: LiveData<MutableList<PeminjamanSuratJalan>> =
        _selectedListPeminjaman

    private val _listPeminjaman = MutableLiveData<List<PeminjamanSuratJalan>>(emptyList())
    val listPeminjaman: LiveData<List<PeminjamanSuratJalan>> = _listPeminjaman

    private val _messageResponse = MutableLiveData<Event<String?>>()
    val messageResponse: LiveData<Event<String?>> = _messageResponse
    fun getAvailablePeminjamanByProyek() {
        _tipe.value?.let { tipe ->
            _proyekId.value?.let { proyekId ->
                viewModelScope.launch {
                    getAvailablePeminjamanByProyekUseCase.execute(
                        tipe,
                        proyekId
                    ).collect {
                        when (it) {
                            is Resource.Loading -> _state.value = StateResponse.LOADING
                            is Resource.Success -> {
                                _state.value = StateResponse.SUCCESS
                                _listPeminjaman.value = it.data!!.toMutableList()
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
    }

    fun resetPeminjaman() {
        _selectedListPeminjaman.value = mutableListOf()
    }

    fun addPeminjaman(peminjaman: PeminjamanSuratJalan) {
        val newValue = _selectedListPeminjaman.value ?: mutableListOf()
        newValue.add(peminjaman)
        _selectedListPeminjaman.value = newValue
    }

    fun removePeminjaman(peminjaman: PeminjamanSuratJalan) {
        val newValue = _selectedListPeminjaman.value ?: mutableListOf()
        newValue.remove(peminjaman)
        _selectedListPeminjaman.value = newValue
    }
}



