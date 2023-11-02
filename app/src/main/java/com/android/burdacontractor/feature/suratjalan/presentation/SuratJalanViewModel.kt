package com.android.burdacontractor.feature.suratjalan.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.android.burdacontractor.core.data.Resource
import com.android.burdacontractor.core.domain.model.Event
import com.android.burdacontractor.core.domain.model.enums.StateResponse
import com.android.burdacontractor.core.domain.model.enums.SuratJalanStatus
import com.android.burdacontractor.core.domain.model.enums.SuratJalanTipe
import com.android.burdacontractor.core.utils.LiveNetworkChecker
import com.android.burdacontractor.feature.suratjalan.domain.model.AllSuratJalan
import com.android.burdacontractor.feature.suratjalan.domain.usecase.GetAllSuratJalanUseCase
import com.android.burdacontractor.feature.suratjalan.domain.usecase.GetCountActiveSuratJalanUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SuratJalanViewModel @Inject constructor(
    val liveNetworkChecker: LiveNetworkChecker,
    private val allSuratJalanUseCase: GetAllSuratJalanUseCase,
    private val getCountActiveSuratJalanUseCase: GetCountActiveSuratJalanUseCase,
) : ViewModel() {

    private val _state = MutableLiveData<StateResponse?>()
    val state: LiveData<StateResponse?> = _state

    private val _messageResponse = MutableLiveData<Event<String?>>()
    val messageResponse : LiveData<Event<String?>> = _messageResponse

    private val _totalActiveSuratJalan = MutableLiveData<Int>()
    val totalActiveSuratJalan: LiveData<Int> = _totalActiveSuratJalan

    private val _status = MutableLiveData<SuratJalanStatus>()
    val status: LiveData<SuratJalanStatus> = _status

    private val _dateStart = MutableLiveData<String?>()
    val dateStart: LiveData<String?> = _dateStart

    private val _dateEnd = MutableLiveData<String?>()
    val dateEnd: LiveData<String?> = _dateEnd

    init {
        getCountActiveSuratJalan()
        _status.value = SuratJalanStatus.MENUNGGU_KONFIRMASI_DRIVER
        _dateStart.value = null
        _dateEnd.value = null
    }
    fun setStatus(status: SuratJalanStatus){
        _status.value = status
    }
    fun setDate(date_start: String, date_end: String){
        _dateStart.value = date_start
        _dateEnd.value = date_end
    }

    fun getAllSuratJalan(
        tipe: SuratJalanTipe,
        status: SuratJalanStatus,
        date_start: String? = null,
        date_end: String? = null,
        size: Int = 10,
        search: String? = null
    ): LiveData<PagingData<AllSuratJalan>> =
        allSuratJalanUseCase.execute(
            tipe = tipe,
            status = status,
            date_start = date_start,
            date_end = date_end,
            size = size,
            search = search
        ).cachedIn(viewModelScope).asLiveData()

    fun getCountActiveSuratJalan() {
        viewModelScope.launch {
            getCountActiveSuratJalanUseCase.execute().collect{
                when(it){
                    is Resource.Loading -> _state.value = StateResponse.LOADING
                    is Resource.Success -> {
                        _state.value = StateResponse.SUCCESS
                        _totalActiveSuratJalan.value = it.data!!.totalActive
                    }
                    is Resource.Error -> {
                        _state.value = StateResponse.ERROR
                    }
                }
            }
        }
    }

}



