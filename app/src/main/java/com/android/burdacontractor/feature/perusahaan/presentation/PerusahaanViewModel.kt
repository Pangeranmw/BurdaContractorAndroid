package com.android.burdacontractor.feature.perusahaan.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.android.burdacontractor.core.data.Resource
import com.android.burdacontractor.core.domain.model.Event
import com.android.burdacontractor.core.domain.model.enums.StateResponse
import com.android.burdacontractor.core.utils.LiveNetworkChecker
import com.android.burdacontractor.feature.perusahaan.domain.model.AllPerusahaan
import com.android.burdacontractor.feature.perusahaan.domain.usecase.GetAllPerusahaanUseCase
import com.android.burdacontractor.feature.perusahaan.domain.usecase.GetPerusahaanProvinsiUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PerusahaanViewModel @Inject constructor(
    val liveNetworkChecker: LiveNetworkChecker,
    private val getAllPerusahaanUseCase: GetAllPerusahaanUseCase,
    private val getPerusahaanProvinsiUseCase: GetPerusahaanProvinsiUseCase,
) : ViewModel() {
    private val _state = MutableLiveData<StateResponse?>()
    val state: LiveData<StateResponse?> = _state

    private val _coordinate = MutableLiveData<String?>(null)
    val coordinate: LiveData<String?> = _coordinate

    private val _provinsiIndex = MutableLiveData<Int?>(null)
    val provinsiIndex: LiveData<Int?> = _provinsiIndex

    private val _listProvinsi = MutableLiveData<List<String>>()
    val listProvinsi: LiveData<List<String>> = _listProvinsi

    private val _search = MutableLiveData<String?>(null)
    val search: LiveData<String?> = _search

    private val _messageResponse = MutableLiveData<Event<String?>>()
    val messageResponse: LiveData<Event<String?>> = _messageResponse

    init {
        getPerusahaanProvinsi()
        getAllPerusahaan()
    }

    fun setProvinsiIndex(provinsiIndex: Int?) {
        _provinsiIndex.value = provinsiIndex
    }

    fun setState(state: StateResponse) {
        _state.value = state
    }

    fun setCoordinate(coordinate: String) {
        _coordinate.value = coordinate
    }

    fun setSearch(search: String) {
        _search.value = search
    }

    fun getPerusahaanProvinsi() {
        viewModelScope.launch {
            getPerusahaanProvinsiUseCase.execute().collect {
                when (it) {
                    is Resource.Loading -> _state.value = StateResponse.LOADING
                    is Resource.Success -> {
                        _state.value = StateResponse.SUCCESS
                        _listProvinsi.value = it.data!!
                        _messageResponse.value = Event("Berhasil Mendapatkan Data Provinsi")
                    }

                    is Resource.Error -> {
                        _state.value = StateResponse.ERROR
                        _messageResponse.value = Event(it.message)
                    }
                }
            }
        }
    }

    fun getAllPerusahaan(): LiveData<PagingData<AllPerusahaan>> {
        var filterProv: String? = null
        _provinsiIndex.value?.let { index ->
            _listProvinsi.value?.let {
                filterProv = it[index]
            }
        }
        return getAllPerusahaanUseCase.execute(
            filter = filterProv,
            search = _search.value,
            coordinate = _coordinate.value,
        ).cachedIn(viewModelScope)
    }
}



