package com.android.burdacontractor.feature.suratjalan.presentation.create

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.burdacontractor.core.data.Resource
import com.android.burdacontractor.core.domain.model.Event
import com.android.burdacontractor.core.domain.model.enums.StateResponse
import com.android.burdacontractor.feature.suratjalan.domain.model.PenggunaanSuratJalan
import com.android.burdacontractor.feature.suratjalan.domain.usecase.GetAvailablePenggunaanByProyekUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PilihPenggunaanSuratJalanViewModel @Inject constructor(
    private val getAvailablePenggunaanByProyekUseCase: GetAvailablePenggunaanByProyekUseCase,
) : ViewModel() {
    private val _state = MutableLiveData<StateResponse?>()
    val state: LiveData<StateResponse?> = _state
    private val _tipe = MutableLiveData<String>()
    private val _proyekId = MutableLiveData<String>()

    fun setProyekId(proyekId: String) {
        _proyekId.value = proyekId
    }

    fun setTipe(tipe: String) {
        _tipe.value = tipe
    }

    private val _selectedListPenggunaan =
        MutableLiveData<MutableList<PenggunaanSuratJalan>>(mutableListOf())
    val selectedListPenggunaan: LiveData<MutableList<PenggunaanSuratJalan>> =
        _selectedListPenggunaan

    private val _listPenggunaan = MutableLiveData<List<PenggunaanSuratJalan>>(emptyList())
    val listPenggunaan: LiveData<List<PenggunaanSuratJalan>> = _listPenggunaan

    private val _messageResponse = MutableLiveData<Event<String?>>()
    val messageResponse: LiveData<Event<String?>> = _messageResponse
    fun getAvailablePenggunaanByProyek() {
        _tipe.value?.let { tipe ->
            _proyekId.value?.let { proyekId ->
                viewModelScope.launch {
                    getAvailablePenggunaanByProyekUseCase.execute(
                        tipe,
                        proyekId
                    ).collect {
                        when (it) {
                            is Resource.Loading -> _state.value = StateResponse.LOADING
                            is Resource.Success -> {
                                _state.value = StateResponse.SUCCESS
                                _listPenggunaan.value = it.data!!.toMutableList()
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

    fun resetPenggunaan() {
        _selectedListPenggunaan.value = mutableListOf()
    }

    fun addPenggunaan(penggunaan: PenggunaanSuratJalan) {
        val newValue = _selectedListPenggunaan.value ?: mutableListOf()
        newValue.add(penggunaan)
        _selectedListPenggunaan.value = newValue
    }

    fun removePenggunaan(penggunaan: PenggunaanSuratJalan) {
        val newValue = _selectedListPenggunaan.value ?: mutableListOf()
        newValue.remove(penggunaan)
        _selectedListPenggunaan.value = newValue
    }
}



