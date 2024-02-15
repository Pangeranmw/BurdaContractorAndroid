package com.android.burdacontractor.feature.logistic.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.burdacontractor.core.domain.model.Proyek
import com.android.burdacontractor.feature.gudang.domain.model.AllGudang
import com.android.burdacontractor.feature.logistic.domain.model.AllLogistic
import com.android.burdacontractor.feature.perusahaan.domain.model.AllPerusahaan
import com.android.burdacontractor.feature.suratjalan.domain.model.TempatSuratJalan

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

    private val _proyek = MutableLiveData<Proyek?>(null)
    val proyek: LiveData<Proyek?> = _proyek
    fun setProyek(proyek: Proyek) {
        _proyek.value = proyek
    }

    private val _perusahaan = MutableLiveData<AllPerusahaan?>(null)
    val perusahaan: LiveData<AllPerusahaan?> = _perusahaan
    fun setPerusahaan(perusahaan: AllPerusahaan) {
        _perusahaan.value = perusahaan
    }

    private val _gudang = MutableLiveData<AllGudang?>(null)
    val gudang: LiveData<AllGudang?> = _gudang
    fun setGudang(gudang: AllGudang) {
        _gudang.value = gudang
    }

    private val _tempatAsal = MutableLiveData<List<TempatSuratJalan>>(listOf())
    val tempatAsal: LiveData<List<TempatSuratJalan>> = _tempatAsal
    fun setTempatAsal(tempatAsal: List<TempatSuratJalan>) {
        _tempatAsal.value = tempatAsal
    }
}



