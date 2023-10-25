package com.android.burdacontractor.feature.perusahaan.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.burdacontractor.feature.perusahaan.domain.model.AllPerusahaan

class PilihPerusahaanViewModel : ViewModel() {

    private val _perusahaan = MutableLiveData<AllPerusahaan?>(null)
    val perusahaan: LiveData<AllPerusahaan?> = _perusahaan

    fun setPerusahaan(perusahaan: AllPerusahaan) {
        _perusahaan.value = perusahaan
    }
}



