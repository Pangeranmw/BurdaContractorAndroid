package com.android.burdacontractor.feature.logistic.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.android.burdacontractor.core.domain.model.Event
import com.android.burdacontractor.core.domain.model.enums.StateResponse
import com.android.burdacontractor.core.utils.LiveNetworkChecker
import com.android.burdacontractor.feature.logistic.domain.model.AllLogistic
import com.android.burdacontractor.feature.logistic.domain.usecase.GetAllLogisticUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LogisticViewModel @Inject constructor(
    val liveNetworkChecker: LiveNetworkChecker,
    private val getAllLogisticUseCase: GetAllLogisticUseCase,
) : ViewModel() {
    private val _state = MutableLiveData<StateResponse?>()
    val state: LiveData<StateResponse?> = _state

    private val _search = MutableLiveData<String?>(null)
    val search: LiveData<String?> = _search

    private val _coordinate = MutableLiveData<String?>(null)
    val coordinate: LiveData<String?> = _coordinate

    private val _messageResponse = MutableLiveData<Event<String?>>()
    val messageResponse: LiveData<Event<String?>> = _messageResponse

    init {
        getAllLogistic()
    }

    fun setState(state: StateResponse) {
        _state.value = state
    }

    fun setSearch(search: String) {
        _search.value = search
    }

    fun setCoordinate(coordinate: String) {
        _coordinate.value = coordinate
    }

    fun getAllLogistic(): LiveData<PagingData<AllLogistic>> {
        return getAllLogisticUseCase.execute(
            search = _search.value,
            coordinate = _coordinate.value,
        ).cachedIn(viewModelScope)
    }
}