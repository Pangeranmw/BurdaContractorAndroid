package com.android.burdacontractor.feature.kendaraan.presentation.pantau

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.burdacontractor.core.data.Resource
import com.android.burdacontractor.core.domain.model.Event
import com.android.burdacontractor.core.domain.model.LogisticCoordinate
import com.android.burdacontractor.core.domain.model.enums.StateResponse
import com.android.burdacontractor.core.domain.usecase.LogisticFirebaseUseCase
import com.android.burdacontractor.core.utils.LiveNetworkChecker
import com.android.burdacontractor.feature.proyek.domain.model.ActiveSjDoLocation
import com.android.burdacontractor.feature.proyek.domain.usecase.GetLogisticActiveSjDoLocationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PantauLokasiPengendaraViewModel @Inject constructor(
    val liveNetworkChecker: LiveNetworkChecker,
    private val getLogisticActiveSjDoLocationUseCase: GetLogisticActiveSjDoLocationUseCase,
    private val logisticFirebaseUseCase: LogisticFirebaseUseCase,
) : ViewModel() {

    private val _state = MutableLiveData<StateResponse?>()
    val state: LiveData<StateResponse?> = _state

    private val _messageResponse = MutableLiveData<Event<String?>>()
    val messageResponse: LiveData<Event<String?>> = _messageResponse

    private val _activeLocation = MutableLiveData<List<ActiveSjDoLocation>>()
    val activeLocation: LiveData<List<ActiveSjDoLocation>> = _activeLocation

    private val _logisticCoordinate = MutableLiveData<LogisticCoordinate?>()
    val logisticCoordinate: LiveData<LogisticCoordinate?> = _logisticCoordinate

    fun getLogisticActiveSjDoLocation(logisticId: String) {
        viewModelScope.launch {
            getLogisticActiveSjDoLocationUseCase.execute(logisticId).collect {
                when (it) {
                    is Resource.Loading -> _state.value = StateResponse.LOADING
                    is Resource.Success -> {
                        _state.value = StateResponse.SUCCESS
                        _messageResponse.value = Event(it.message)
                        _activeLocation.value = it.data!!
                    }

                    is Resource.Error -> {
                        _state.value = StateResponse.ERROR
                        _messageResponse.value = Event(it.message)
                    }
                }
            }
        }
    }

    fun getCoordinate(logisticId: String) {
        viewModelScope.launch {
            logisticFirebaseUseCase.getCoordinate(logisticId).collect {
                when (it) {
                    is Resource.Loading -> {
                        _state.value = StateResponse.LOADING
                    }

                    is Resource.Success -> {
                        _state.value = StateResponse.SUCCESS
                        it.data?.let { data ->
                            _logisticCoordinate.postValue(data)
                        }
                    }

                    is Resource.Error -> {
                        _state.value = StateResponse.ERROR
                    }
                }
            }
        }
    }
}



