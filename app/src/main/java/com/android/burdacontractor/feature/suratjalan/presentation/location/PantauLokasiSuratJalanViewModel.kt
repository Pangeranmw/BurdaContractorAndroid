package com.android.burdacontractor.feature.suratjalan.presentation.location

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.burdacontractor.core.data.Resource
import com.android.burdacontractor.core.data.source.remote.response.DistanceMatrixResponse
import com.android.burdacontractor.core.domain.model.LogisticCoordinate
import com.android.burdacontractor.core.domain.model.enums.StateResponse
import com.android.burdacontractor.core.domain.usecase.GetDistanceMatrixUseCase
import com.android.burdacontractor.core.domain.usecase.LogisticFirebaseUseCase
import com.android.burdacontractor.core.utils.LiveNetworkChecker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PantauLokasiSuratJalanViewModel @Inject constructor(
    val liveNetworkChecker: LiveNetworkChecker,
    private val getDistanceMatrixUseCase: GetDistanceMatrixUseCase,
    private val logisticFirebaseUseCase: LogisticFirebaseUseCase,
) : ViewModel() {

    private val _state = MutableLiveData<StateResponse?>()
    val state: LiveData<StateResponse?> = _state

    private val _distanceMatrix = MutableLiveData<DistanceMatrixResponse>()
    val distanceMatrix: LiveData<DistanceMatrixResponse> = _distanceMatrix

    private val _logisticCoordinate = MutableLiveData<LogisticCoordinate?>()
    val logisticCoordinate: LiveData<LogisticCoordinate?> = _logisticCoordinate

    fun getDistanceMatrix(coordinates: String) {
        viewModelScope.launch {
            getDistanceMatrixUseCase.execute(coordinates).collect {
                when (it) {
                    is Resource.Loading -> _state.value = StateResponse.LOADING
                    is Resource.Success -> {
                        _state.value = StateResponse.SUCCESS
                        _distanceMatrix.value = it.data!!
                    }

                    is Resource.Error -> {
                        _state.value = StateResponse.ERROR
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



