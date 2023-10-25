package com.android.burdacontractor.feature.gudang.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.burdacontractor.feature.gudang.domain.model.AllGudang

class PilihGudangViewModel : ViewModel() {

    private val _gudang = MutableLiveData<AllGudang?>(null)
    val gudang: LiveData<AllGudang?> = _gudang
    fun setGudang(gudang: AllGudang) {
        _gudang.value = gudang
    }
}



