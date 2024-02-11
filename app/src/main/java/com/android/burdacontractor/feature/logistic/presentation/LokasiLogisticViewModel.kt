package com.android.burdacontractor.feature.logistic.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.burdacontractor.core.data.Resource
import com.android.burdacontractor.core.domain.model.Event
import com.android.burdacontractor.core.domain.model.enums.StateResponse
import com.android.burdacontractor.core.utils.LiveNetworkChecker
import com.android.burdacontractor.feature.logistic.domain.model.AllLogistic
import com.android.burdacontractor.feature.logistic.domain.usecase.GetAllLogisticNoPagingUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LokasiLogisticViewModel @Inject constructor(
    val liveNetworkChecker: LiveNetworkChecker,
    private val getAllLogisticNoPagingUseCase: GetAllLogisticNoPagingUseCase,
) : ViewModel() {
    private val _state = MutableLiveData<StateResponse?>()
    val state: LiveData<StateResponse?> = _state

    private val _messageResponse = MutableLiveData<Event<String?>>()
    val messageResponse: LiveData<Event<String?>> = _messageResponse

    private val _listLogistic = MutableLiveData<List<AllLogistic>>()
    val listLogistic: LiveData<List<AllLogistic>> = _listLogistic

    init {
        getAllLogisticNoPaging()
    }

    fun getAllLogisticNoPaging() {
        viewModelScope.launch {
            getAllLogisticNoPagingUseCase.execute().collect {
                when (it) {
                    is Resource.Loading -> _state.value = StateResponse.LOADING
                    is Resource.Success -> {
                        _state.value = StateResponse.SUCCESS
                        _listLogistic.value = it.data!!
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