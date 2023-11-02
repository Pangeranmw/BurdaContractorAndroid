package com.android.burdacontractor.feature.kendaraan.presentation.create

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.burdacontractor.core.data.Resource
import com.android.burdacontractor.core.domain.model.Event
import com.android.burdacontractor.core.domain.model.enums.StateResponse
import com.android.burdacontractor.core.utils.LiveNetworkChecker
import com.android.burdacontractor.feature.kendaraan.domain.usecase.AddKendaraanUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class AddKendaraanViewModel @Inject constructor(
    val liveNetworkChecker: LiveNetworkChecker,
    private val addKendaraanUseCase: AddKendaraanUseCase,
) : ViewModel() {
    private val _state = MutableLiveData<StateResponse?>()
    val state: LiveData<StateResponse?> = _state

    private val _messageResponse = MutableLiveData<Event<String?>>()
    val messageResponse: LiveData<Event<String?>> = _messageResponse

    fun addKendaraan(
        gudangId: String,
        jenis: String,
        merk: String,
        platNomor: String,
        gambar: File,
        successListener: (String) -> Unit
    ) {
        viewModelScope.launch {
            addKendaraanUseCase.execute(gudangId, jenis, merk, platNomor, gambar).collect {
                when (it) {
                    is Resource.Loading -> _state.value = StateResponse.LOADING
                    is Resource.Success -> {
                        _state.value = StateResponse.SUCCESS
                        val id = it.data!!
                        _messageResponse.value = Event(it.message)
                        successListener(id)
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



