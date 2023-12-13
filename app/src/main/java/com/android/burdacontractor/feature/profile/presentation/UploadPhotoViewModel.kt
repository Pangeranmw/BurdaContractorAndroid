package com.android.burdacontractor.feature.profile.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.burdacontractor.core.data.Resource
import com.android.burdacontractor.core.domain.model.Event
import com.android.burdacontractor.core.domain.model.enums.StateResponse
import com.android.burdacontractor.core.utils.LiveNetworkChecker
import com.android.burdacontractor.feature.profile.domain.usecase.UploadPhotoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class UploadPhotoViewModel @Inject constructor(
    val liveNetworkChecker: LiveNetworkChecker,
    private val uploadPhotoUseCase: UploadPhotoUseCase,
) : ViewModel() {

    private val _state = MutableLiveData<StateResponse?>()
    val state: LiveData<StateResponse?> = _state

    private val _messageResponse = MutableLiveData<Event<String>>()
    val messageResponse: LiveData<Event<String>> = _messageResponse

    fun uploadPhoto(photo: File) {
        viewModelScope.launch {
            uploadPhotoUseCase.execute(photo).collect {
                when (it) {
                    is Resource.Loading -> _state.value = StateResponse.LOADING
                    is Resource.Success -> {
                        _state.value = StateResponse.SUCCESS
                        _messageResponse.value = Event(it.message.toString())
                    }

                    is Resource.Error -> {
                        _state.value = StateResponse.ERROR
                        _messageResponse.value = Event(it.message.toString())
                    }
                }
            }
        }
    }
}



