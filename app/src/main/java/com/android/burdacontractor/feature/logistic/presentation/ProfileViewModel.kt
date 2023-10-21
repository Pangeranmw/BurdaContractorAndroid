package com.android.burdacontractor.feature.logistic.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.burdacontractor.core.data.Resource
import com.android.burdacontractor.core.domain.model.Event
import com.android.burdacontractor.core.domain.model.enums.StateResponse
import com.android.burdacontractor.core.domain.usecase.LogisticFirebaseUseCase
import com.android.burdacontractor.core.domain.usecase.StorageUseCase
import com.android.burdacontractor.core.utils.LiveNetworkChecker
import com.android.burdacontractor.feature.auth.domain.usecase.LogoutUseCase
import com.android.burdacontractor.feature.profile.data.source.remote.response.UserByTokenItem
import com.android.burdacontractor.feature.profile.domain.usecase.GetUserByTokenUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    val liveNetworkChecker: LiveNetworkChecker,
    private val getUserByTokenUseCase: GetUserByTokenUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val logisticFirebaseUseCase: LogisticFirebaseUseCase,
    private val storageUseCase: StorageUseCase,
) : ViewModel() {

    private val _state = MutableLiveData<StateResponse?>()
    val state: LiveData<StateResponse?> = _state

    private val _isTracking = MutableLiveData<Boolean>()
    val isTracking: LiveData<Boolean> = _isTracking

    private val _user = MutableLiveData<UserByTokenItem>()
    val user: LiveData<UserByTokenItem> = _user

    private val _messageResponse = MutableLiveData<Event<String?>>()
    val messageResponse: LiveData<Event<String?>> = _messageResponse

    init {
        getUserByToken()
    }

    fun setIsTrackingStorage(isTracking: Boolean) {
        storageUseCase.setTracking(isTracking)
    }

    fun setIsTrackingRealtime(logisticId: String, logisticIsTracking: Boolean) {
        viewModelScope.launch {
            logisticFirebaseUseCase.setIsTracking(logisticId, logisticIsTracking)
        }
    }

    private fun getUserByToken() {
        viewModelScope.launch {
            getUserByTokenUseCase.execute().collect {
                when (it) {
                    is Resource.Loading -> _state.value = StateResponse.LOADING
                    is Resource.Success -> {
                        _state.value = StateResponse.SUCCESS
                        _user.value = it.data!!
                    }

                    is Resource.Error -> {
                        _state.value = StateResponse.ERROR
                    }
                }
            }
        }
    }

    fun logout(listener: () -> Unit) {
        viewModelScope.launch {
            logoutUseCase.execute().collect {
                when (it) {
                    is Resource.Loading -> _state.value = StateResponse.LOADING
                    is Resource.Success -> {
                        val data = it.data
                        if (data != null) {
                            _messageResponse.value = Event(data.message)
                        }
                        listener()
                    }

                    is Resource.Error -> {
                        _state.value = StateResponse.ERROR
                        _messageResponse.value = Event(it.message)
                    }
                }
            }
        }
    }

    val getIsTrackingStorage = storageUseCase.getTracking()
    fun getIsTrackingRealtime(logisticId: String) {
        viewModelScope.launch {
            logisticFirebaseUseCase.getTracking(logisticId).collect {
                when (it) {
                    is Resource.Loading -> {
                        _state.value = StateResponse.LOADING
                    }

                    is Resource.Success -> {
                        _state.value = StateResponse.SUCCESS
                        it.data?.let { data ->
                            _isTracking.postValue(data.isTracking)
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