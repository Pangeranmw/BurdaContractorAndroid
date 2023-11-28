package com.android.burdacontractor.feature.suratjalan.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.burdacontractor.feature.suratjalan.domain.model.PeminjamanBarangTidakHabisPakaiItem

class BarangPeminjamanSuratJalanViewModel : ViewModel() {

    private val _barang = MutableLiveData<PeminjamanBarangTidakHabisPakaiItem?>(null)
    val barang: LiveData<PeminjamanBarangTidakHabisPakaiItem?> = _barang
    fun setBarang(barang: PeminjamanBarangTidakHabisPakaiItem?) {
        _barang.value = barang
    }
}



