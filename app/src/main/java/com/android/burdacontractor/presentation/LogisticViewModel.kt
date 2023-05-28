package com.android.burdacontractor.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.burdacontractor.core.data.Resource
import com.android.burdacontractor.core.domain.model.LogisticCoordinate
import com.android.burdacontractor.core.domain.usecase.LogisticUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LogisticViewModel @Inject constructor(private val logisticUseCase: LogisticUseCase) : ViewModel() {
    private val _logisticCoordinate = MutableLiveData<Resource<LogisticCoordinate>>()
    val logisticCoordinate: LiveData<Resource<LogisticCoordinate>> = _logisticCoordinate

    fun getCoordinate(logisticId: String){
        viewModelScope.launch {
            logisticUseCase.getCoordinate(logisticId).collect {
                when(it) {
                    is Resource.Loading -> {
                        _logisticCoordinate.postValue(Resource.Loading(null))
                    }
                    is Resource.Success -> {
                        it.data?.let { data ->
                            _logisticCoordinate.postValue(Resource.Success(data))
                        }
                    }
                    is Resource.Error -> {  _logisticCoordinate.postValue(Resource.Error("Failed to grab items from Firebase", null)) }
                }
            }
        }
    }
    fun setCoordinate(logisticId: String, logisticCoordinate: LogisticCoordinate){
        viewModelScope.launch {
            logisticUseCase.setCoordinate(logisticId, logisticCoordinate)
        }
    }
}

