package com.android.burdacontractor.feature.gudang.presentation.main

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
import com.android.burdacontractor.feature.gudang.domain.model.AllGudang
import com.android.burdacontractor.feature.gudang.domain.usecase.GetAllGudangUseCase
import com.android.burdacontractor.feature.gudang.domain.usecase.GetGudangProvinsiUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GudangViewModel @Inject constructor(
    val liveNetworkChecker: LiveNetworkChecker,
    private val getAllGudangUseCase: GetAllGudangUseCase,
    private val getGudangProvinsiUseCase: GetGudangProvinsiUseCase,
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

    fun getGudangProvinsi() {
        viewModelScope.launch {
            getGudangProvinsiUseCase.execute().collect {
                when (it) {
                    is Resource.Loading -> _state.value = StateResponse.LOADING
                    is Resource.Success -> {
                        _listProvinsi.value = it.data!!
                    }
                    is Resource.Error -> {
                        _state.value = StateResponse.ERROR
                        _messageResponse.value = Event(it.message)
                    }
                }
            }
        }
    }

    fun getAllGudang(): LiveData<PagingData<AllGudang>> {
        var filterProv: String? = null
        _provinsiIndex.value?.let { index ->
            _listProvinsi.value?.let {
                filterProv = it[index]
            }
        }
        return getAllGudangUseCase.execute(
            filter = filterProv,
            search = _search.value,
            coordinate = _coordinate.value,
        ).cachedIn(viewModelScope)
    }
}