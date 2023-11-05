package com.android.burdacontractor.feature.perusahaan.presentation.create

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.burdacontractor.core.data.Resource
import com.android.burdacontractor.core.domain.model.Event
import com.android.burdacontractor.core.domain.model.enums.StateResponse
import com.android.burdacontractor.core.domain.usecase.GetCityByProvinceUseCase
import com.android.burdacontractor.core.domain.usecase.GetProvinceUseCase
import com.android.burdacontractor.core.utils.LiveNetworkChecker
import com.android.burdacontractor.feature.perusahaan.domain.usecase.AddPerusahaanUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class AddPerusahaanViewModel @Inject constructor(
    val liveNetworkChecker: LiveNetworkChecker,
    private val addPerusahaanUseCase: AddPerusahaanUseCase,
    private val getProvinceUseCase: GetProvinceUseCase,
    private val getCityByProvinceUseCase: GetCityByProvinceUseCase,
) : ViewModel() {
    private val _state = MutableLiveData<StateResponse?>()
    val state: LiveData<StateResponse?> = _state

    private val _messageResponse = MutableLiveData<Event<String?>>()
    val messageResponse: LiveData<Event<String?>> = _messageResponse

    private val _province = MutableLiveData<List<String>>(null)
    val province: LiveData<List<String>> = _province

    private val _city = MutableLiveData<List<String>>(null)
    val city: LiveData<List<String>> = _city

    init {
        getProvince()
    }

    fun addPerusahaan(
        nama: String,
        alamat: String,
        provinsi: String,
        kota: String,
        latitude: String,
        longitude: String,
        gambar: File,
        successListener: (String) -> Unit
    ) {
        viewModelScope.launch {
            addPerusahaanUseCase.execute(nama, alamat, provinsi, kota, latitude, longitude, gambar)
                .collect {
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

    fun getProvince() {
        viewModelScope.launch {
            getProvinceUseCase.execute().collect {
                when (it) {
                    is Resource.Loading -> _state.value = StateResponse.LOADING
                    is Resource.Success -> {
                        _state.value = StateResponse.SUCCESS
                        _province.value = it.data!!
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

    fun getCityByProvince(provinsi: String) {
        viewModelScope.launch {
            getCityByProvinceUseCase.execute(provinsi).collect {
                when (it) {
                    is Resource.Loading -> _state.value = StateResponse.LOADING
                    is Resource.Success -> {
                        _state.value = StateResponse.SUCCESS
                        _city.value = it.data!!
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



