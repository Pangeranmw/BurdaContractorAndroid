package com.android.burdacontractor.feature.kendaraan.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.burdacontractor.feature.kendaraan.domain.model.AllKendaraan

class PilihKendaraanViewModel : ViewModel() {

    private val _kendaraan = MutableLiveData<AllKendaraan?>(null)
    val kendaraan: LiveData<AllKendaraan?> = _kendaraan
    fun setKendaraan(kendaraan: AllKendaraan) {
        _kendaraan.value = kendaraan
    }
}



