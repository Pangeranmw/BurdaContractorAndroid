package com.android.burdacontractor.presentation.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.burdacontractor.core.data.Resource
import com.android.burdacontractor.core.data.source.remote.response.ErrorMessageResponse
import com.android.burdacontractor.core.data.source.remote.response.LoginItem
import com.android.burdacontractor.core.data.source.remote.response.LoginResponse
import com.android.burdacontractor.core.domain.model.StateResponse
import com.android.burdacontractor.core.domain.model.UserRole
import com.android.burdacontractor.core.domain.usecase.AuthUseCase
import com.android.burdacontractor.core.domain.usecase.StorageUseCase
import com.android.burdacontractor.core.utils.LiveNetworkChecker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(val liveNetworkChecker: LiveNetworkChecker,private val authUseCase: AuthUseCase, private val storageUseCase: StorageUseCase) : ViewModel() {
    val isUserLogin = storageUseCase.isUserLogin()

    private val _state = MutableLiveData<StateResponse?>()
    val state: LiveData<StateResponse?> = _state

    private val _loginResponse = MutableLiveData<LoginItem?>()
    val loginResponse : LiveData<LoginItem?> = _loginResponse

    private val _loginMessageResponse = MutableLiveData<String?>()
    val loginMessageResponse : LiveData<String?> = _loginMessageResponse

    fun loginUser(userId:String, token: String, role: UserRole) {
        storageUseCase.loginUser(
            userId,
            token,
            role.name
        )
    }

    fun register(
        nama: String,
        noHp: String,
        email: String,
        password: String,
    ) {
        viewModelScope.launch {
            authUseCase.register(nama,noHp,email,password).collect{
                when(it){
                    is Resource.Loading -> _state.value = StateResponse.LOADING
                    is Resource.Success -> {
                        _state.value = StateResponse.SUCCESS
                    }
                    is Resource.Error -> {
                        _state.value = StateResponse.ERROR
                    }
                }
            }
        }
    }

    fun login(email: String, password: String){
        viewModelScope.launch {
            authUseCase.login(email,password).collect{
                when(it){
                    is Resource.Loading -> _state.value = StateResponse.LOADING
                    is Resource.Success -> {
                        _state.value = StateResponse.SUCCESS
                        val data = it.data
                        if(data?.user != null){
                            _loginResponse.value = data.user
                            _loginMessageResponse.value = data.message
                        }
                    }
                    is Resource.Error -> {
                        _state.value = StateResponse.ERROR
                        _loginMessageResponse.value = it.message
                    }
                }
            }
        }
    }

    fun loginWithPin(pin: String) {
        viewModelScope.launch {
            authUseCase.loginWithPin(pin).collect{
                when(it){
                    is Resource.Loading -> _state.value = StateResponse.LOADING
                    is Resource.Success -> {
                        _state.value = StateResponse.SUCCESS
                        val data = it.data
                        if(data != null){
                            _loginMessageResponse.value = data.message
                        }
                    }
                    is Resource.Error -> {
                        _state.value = StateResponse.ERROR
                        _loginMessageResponse.value = it.message
                    }
                }
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            authUseCase.logout().collect{
                when(it){
                    is Resource.Loading -> _state.value = StateResponse.LOADING
                    is Resource.Success -> {
                        _state.value = StateResponse.SUCCESS
                        val data = it.data
                        if(data != null){
                            _loginMessageResponse.value = data.message
                        }
                    }
                    is Resource.Error -> {
                        _state.value = StateResponse.ERROR
                        _loginMessageResponse.value = it.message
                    }
                }
            }
        }
    }

}



