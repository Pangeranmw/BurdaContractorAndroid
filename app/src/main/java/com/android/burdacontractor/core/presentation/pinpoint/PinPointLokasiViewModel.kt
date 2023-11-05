package com.android.burdacontractor.core.presentation.pinpoint

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.burdacontractor.core.data.Resource
import com.android.burdacontractor.core.domain.usecase.GetLocationFromAddressUseCase
import com.android.burdacontractor.core.domain.usecase.GetLocationFromCoordinateUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PinPointLokasiViewModel @Inject constructor(
    private val getLocationFromAddressUseCase: GetLocationFromAddressUseCase,
    private val getLocationFromCoordinateUseCase: GetLocationFromCoordinateUseCase,
) : ViewModel() {

    private val _alamat = MutableLiveData<String?>(null)
    val alamat: LiveData<String?> = _alamat

    fun getLocationFromCoordinate(listener: () -> Unit) {
        viewModelScope.launch {
            _latitude.value?.let { lat ->
                _longitude.value?.let { lon ->
                    getLocationFromCoordinateUseCase.execute(lat, lon).collect {
                        when (it) {
                            is Resource.Loading -> {}
                            is Resource.Success -> {
                                _alamat.value = it.data!!.displayName
                                listener()
                            }

                            is Resource.Error -> {
                                Log.d("getLocationFromCoordinate", it.message.toString())
                                listener()
                            }
                        }
                    }
                }
            }
        }
    }

    private val _latitude = MutableLiveData<String?>(null)
    val latitude: LiveData<String?> = _latitude

    fun setLatitude(latitude: String) {
        _latitude.value = latitude
    }

    private val _longitude = MutableLiveData<String?>(null)
    val longitude: LiveData<String?> = _longitude

    fun setLongitude(longitude: String) {
        _longitude.value = longitude
    }
}



