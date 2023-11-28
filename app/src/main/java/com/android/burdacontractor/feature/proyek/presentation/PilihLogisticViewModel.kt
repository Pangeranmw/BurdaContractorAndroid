package com.android.burdacontractor.feature.proyek.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.burdacontractor.feature.proyek.domain.model.AllLogistic

class PilihLogisticViewModel : ViewModel() {

    private val _logistic = MutableLiveData<AllLogistic?>(null)
    val logistic: LiveData<AllLogistic?> = _logistic
    fun setLogistic(logistic: AllLogistic) {
        _logistic.value = logistic
    }
}



