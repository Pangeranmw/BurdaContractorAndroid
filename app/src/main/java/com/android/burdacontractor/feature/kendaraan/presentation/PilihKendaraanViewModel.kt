package com.android.burdacontractor.feature.kendaraan.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.burdacontractor.feature.kendaraan.domain.model.Kendaraan

class PilihKendaraanViewModel : ViewModel() {

    private val _kendaraan = MutableLiveData<Kendaraan?>(null)
    val kendaraan: LiveData<Kendaraan?> = _kendaraan
    fun setKendaraan(kendaraan: Kendaraan) {
        _kendaraan.value = kendaraan
    }
}



