package com.android.burdacontractor.feature.profile.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.burdacontractor.core.data.Resource
import com.android.burdacontractor.core.domain.model.Event
import com.android.burdacontractor.core.domain.model.enums.StateResponse
import com.android.burdacontractor.core.utils.LiveNetworkChecker
import com.android.burdacontractor.feature.profile.data.source.remote.response.UserByTokenItem
import com.android.burdacontractor.feature.profile.domain.usecase.GetUserByTokenUseCase
import com.android.burdacontractor.feature.profile.domain.usecase.UploadTtdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import javax.inject.Inject

@HiltViewModel
class SignatureViewModel @Inject constructor(
    val liveNetworkChecker: LiveNetworkChecker,
    private val getUserByTokenUseCase: GetUserByTokenUseCase,
    private val uploadTtdUseCase: UploadTtdUseCase,
) : ViewModel() {

    private val _state = MutableLiveData<StateResponse?>()
    val state: LiveData<StateResponse?> = _state

    private val _user = MutableLiveData<UserByTokenItem>()
    val user: LiveData<UserByTokenItem> = _user

    private val _messageResponse = MutableLiveData<Event<String?>>()
    val messageResponse : LiveData<Event<String?>> = _messageResponse

    init {
        getUserByToken()
    }

    fun getUserByToken(){
        viewModelScope.launch {
            getUserByTokenUseCase.execute().collect{
                when(it){
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
    fun uploadTtd(ttd: MultipartBody.Part){
        viewModelScope.launch {
            uploadTtdUseCase.execute(ttd).collect{
                when(it){
                    is Resource.Loading -> _state.value = StateResponse.LOADING
                    is Resource.Success -> {
                        _state.value = StateResponse.SUCCESS
                        _messageResponse.value = Event(it.message)
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