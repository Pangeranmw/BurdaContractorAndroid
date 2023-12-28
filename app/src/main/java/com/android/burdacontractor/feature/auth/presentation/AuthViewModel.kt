package com.android.burdacontractor.feature.auth.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.burdacontractor.core.data.Resource
import com.android.burdacontractor.core.domain.model.Event
import com.android.burdacontractor.core.domain.model.enums.StateResponse
import com.android.burdacontractor.core.utils.LiveNetworkChecker
import com.android.burdacontractor.feature.auth.domain.usecase.ForgetPasswordUseCase
import com.android.burdacontractor.feature.auth.domain.usecase.LoginUseCase
import com.android.burdacontractor.feature.auth.domain.usecase.RegisterUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    val liveNetworkChecker: LiveNetworkChecker,
    private val loginUseCase: LoginUseCase,
    private val registerUseCase: RegisterUseCase,
    private val forgetPasswordUseCase: ForgetPasswordUseCase,
) : ViewModel() {

    private val _state = MutableLiveData<StateResponse>()
    val state: LiveData<StateResponse> = _state

    private val _messageResponse = MutableLiveData<Event<String?>>()
    val messageResponse: LiveData<Event<String?>> = _messageResponse

    fun register(
        nama: String,
        noHp: String,
        email: String,
        password: String,
        listener: () -> Unit,
    ) {
        viewModelScope.launch {
            registerUseCase.execute(nama,noHp,email,password).collect{
                when(it){
                    is Resource.Loading -> _state.value = StateResponse.LOADING
                    is Resource.Success -> {
                        _state.value = StateResponse.SUCCESS
                        val data = it.data!!
                        _messageResponse.value = Event(data.message)
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

    fun login(email: String, password: String, listener: () -> Unit) {
        viewModelScope.launch {
            loginUseCase.execute(email, password).collect {
                when (it) {
                    is Resource.Loading -> _state.value = StateResponse.LOADING
                    is Resource.Success -> {
                        _state.value = StateResponse.SUCCESS
                        val data = it.data
                        if (data != null) {
                            _messageResponse.value = Event(it.message)
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

    fun forgetPassword(email: String) {
        viewModelScope.launch {
            forgetPasswordUseCase.execute(email).collect {
                when (it) {
                    is Resource.Loading -> _state.value = StateResponse.LOADING
                    is Resource.Success -> {
                        _state.value = StateResponse.SUCCESS
                        val data = it.data
                        if (data != null) {
                            _messageResponse.value = Event(it.message)
                        }
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



