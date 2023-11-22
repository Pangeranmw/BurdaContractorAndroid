package com.android.burdacontractor.feature.suratjalan.presentation.uploadphoto

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.burdacontractor.core.data.Resource
import com.android.burdacontractor.core.domain.model.Event
import com.android.burdacontractor.core.domain.model.enums.StateResponse
import com.android.burdacontractor.core.utils.LiveNetworkChecker
import com.android.burdacontractor.feature.suratjalan.domain.usecase.UploadFotoBuktiSuratJalanUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class UploadFotoBuktiSuratJalanViewModel @Inject constructor(
    val liveNetworkChecker: LiveNetworkChecker,
    private val uploadFotoBuktiSuratJalanUseCase: UploadFotoBuktiSuratJalanUseCase,
) : ViewModel() {

    private val _state = MutableLiveData<StateResponse?>()
    val state: LiveData<StateResponse?> = _state

    private val _messageResponse = MutableLiveData<Event<String>>()
    val messageResponse: LiveData<Event<String>> = _messageResponse

    fun uploadFotoBukti(id: String, fotoBukti: File) {
        viewModelScope.launch {
            uploadFotoBuktiSuratJalanUseCase.execute(id, fotoBukti).collect {
                when (it) {
                    is Resource.Loading -> _state.value = StateResponse.LOADING
                    is Resource.Success -> {
                        _state.value = StateResponse.SUCCESS
                        if (it.data != null)
                            _messageResponse.value = Event(it.data.message)
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



