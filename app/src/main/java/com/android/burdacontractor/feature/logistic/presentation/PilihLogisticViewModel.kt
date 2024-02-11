package com.android.burdacontractor.feature.logistic.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.burdacontractor.feature.logistic.domain.model.AllLogistic

class PilihLogisticViewModel : ViewModel() {

    private val _logistic = MutableLiveData<AllLogistic?>(null)
    val logistic: LiveData<AllLogistic?> = _logistic
    fun setLogistic(logistic: AllLogistic) {
        _logistic.value = logistic
    }

    private val _longitude = MutableLiveData<String?>(null)
    val longitude: LiveData<String?> = _longitude

    fun setLongitude(longitude: String) {
        _longitude.value = longitude
    }

    private val _latitude = MutableLiveData<String?>(null)
    val latitude: LiveData<String?> = _latitude

    fun setLatitude(latitude: String) {
        _latitude.value = latitude
    }
}



