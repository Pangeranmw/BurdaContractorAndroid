package com.android.burdacontractor.core.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.burdacontractor.core.data.Resource
import com.android.burdacontractor.core.domain.model.LogisticCoordinate
import com.android.burdacontractor.core.domain.usecase.LogisticFirebaseUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LogisticFirebaseViewModel @Inject constructor(private val logisticFirebaseUseCase: LogisticFirebaseUseCase) :
    ViewModel() {
    private val _logisticCoordinate = MutableLiveData<Resource<LogisticCoordinate>>()
    val logisticCoordinate: LiveData<Resource<LogisticCoordinate>> = _logisticCoordinate

    private val _isTracking = MutableLiveData<Boolean>()
    val isTracking: LiveData<Boolean> = _isTracking

    fun getCoordinate(logisticId: String) {
        viewModelScope.launch {
            logisticFirebaseUseCase.getCoordinate(logisticId).collect {
                when (it) {
                    is Resource.Loading -> {
                        _logisticCoordinate.postValue(Resource.Loading(null))
                    }

                    is Resource.Success -> {
                        it.data?.let { data ->
                            _logisticCoordinate.postValue(Resource.Success(data))
                        }
                    }

                    is Resource.Error -> {
                        _logisticCoordinate.postValue(
                            Resource.Error(
                                "Failed to grab items from Firebase",
                                null
                            )
                        )
                    }
                }
            }
        }
    }
    fun setCoordinate(logisticId: String, logisticCoordinate: LogisticCoordinate){
        viewModelScope.launch {
            logisticFirebaseUseCase.setCoordinate(logisticId, logisticCoordinate)
        }
    }
    fun getIsTrackingRealtime(logisticId: String){
        viewModelScope.launch {
            logisticFirebaseUseCase.getTracking(logisticId).collect {
                when (it) {
                    is Resource.Loading -> {}
                    is Resource.Success -> {
                        it.data?.let { data ->
                            _isTracking.postValue(data.isTracking)
                        }
                    }

                    is Resource.Error -> {}
                }
            }
        }
    }
}

