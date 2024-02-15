package com.android.burdacontractor.feature.suratjalan.presentation.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.android.burdacontractor.core.data.Resource
import com.android.burdacontractor.core.domain.model.Event
import com.android.burdacontractor.core.domain.model.enums.CreatedByOrFor
import com.android.burdacontractor.core.domain.model.enums.StateResponse
import com.android.burdacontractor.core.domain.model.enums.SuratJalanStatus
import com.android.burdacontractor.core.domain.model.enums.SuratJalanTipe
import com.android.burdacontractor.core.utils.LiveNetworkChecker
import com.android.burdacontractor.feature.profile.data.source.remote.response.UserByTokenItem
import com.android.burdacontractor.feature.profile.domain.usecase.GetUserByTokenUseCase
import com.android.burdacontractor.feature.suratjalan.domain.model.AllSuratJalan
import com.android.burdacontractor.feature.suratjalan.domain.usecase.GetAllSuratJalanUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SuratJalanViewModel @Inject constructor(
    val liveNetworkChecker: LiveNetworkChecker,
    private val getAllSuratJalanUseCase: GetAllSuratJalanUseCase,
    private val getUserByTokenUseCase: GetUserByTokenUseCase,
) : ViewModel() {
    private val _state = MutableLiveData<StateResponse?>()
    val state: LiveData<StateResponse?> = _state

    private val _dateStart = MutableLiveData<String?>()
    val dateStart: LiveData<String?> = _dateStart

    private val _dateEnd = MutableLiveData<String?>()
    val dateEnd: LiveData<String?> = _dateEnd

    private val _createdByOrFor = MutableLiveData(CreatedByOrFor.all)
    val createdByOrFor: LiveData<CreatedByOrFor> = _createdByOrFor

    private val _status = MutableLiveData(SuratJalanStatus.SEMUA)
    val status: LiveData<SuratJalanStatus?> = _status

    private val _search = MutableLiveData<String?>(null)
    val search: LiveData<String?> = _search

    private val _tipe = MutableLiveData(SuratJalanTipe.PENGIRIMAN_GUDANG_PROYEK)
    val tipe: LiveData<SuratJalanTipe> = _tipe

    private val _messageResponse = MutableLiveData<Event<String?>>()
    val messageResponse: LiveData<Event<String?>> = _messageResponse

    private val _user = MutableLiveData<UserByTokenItem>()
    val user: LiveData<UserByTokenItem> = _user

    init {
        getUserByToken()
    }

    fun getUserByToken() {
        viewModelScope.launch {
            getUserByTokenUseCase.execute().collect {
                when (it) {
                    is Resource.Loading -> _state.value = StateResponse.LOADING
                    is Resource.Success -> {
                        _user.value = it.data!!
                    }

                    is Resource.Error -> {
                        _state.value = StateResponse.ERROR
                    }
                }
            }
        }
    }

    fun setStatus(status: SuratJalanStatus) {
        _status.value = status
    }

    fun setTipe(tipe: SuratJalanTipe) {
        _tipe.value = tipe
    }

    fun setState(state: StateResponse) {
        _state.value = state
    }

    fun setCreatedByOrFor(createdByOrFor: CreatedByOrFor) {
        _createdByOrFor.value = createdByOrFor
    }

    fun setSearch(search: String) {
        _search.value = search
    }

    fun setDate(dateStart: String?, dateEnd: String?) {
        _dateStart.value = dateStart
        _dateEnd.value = dateEnd
    }

    fun getAllSuratJalan(): LiveData<PagingData<AllSuratJalan>> =
        getAllSuratJalanUseCase.execute(
            size = 30,
            tipe = _tipe.value!!,
            status = _status.value!!,
            dateStart = _dateStart.value,
            dateEnd = _dateEnd.value,
            search = _search.value,
            createdByOrFor = _createdByOrFor.value!!
        ).cachedIn(viewModelScope).asLiveData()
}



