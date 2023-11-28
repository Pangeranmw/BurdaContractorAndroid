package com.android.burdacontractor.feature.suratjalan.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.burdacontractor.feature.suratjalan.domain.model.PenggunaanBarangHabisPakaiItem

class BarangPenggunaanSuratJalanViewModel : ViewModel() {

    private val _barang = MutableLiveData<PenggunaanBarangHabisPakaiItem?>(null)
    val barang: LiveData<PenggunaanBarangHabisPakaiItem?> = _barang
    fun setBarang(barang: PenggunaanBarangHabisPakaiItem?) {
        _barang.value = barang
    }
}



