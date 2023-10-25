package com.android.burdacontractor.feature.kendaraan.domain.usecase

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.android.burdacontractor.feature.kendaraan.domain.model.AllKendaraan

interface GetAllKendaraanUseCase {
    fun execute(
        size: Int = 5,
        filter: String? = null,
        gudang: String? = null,
        status: String? = null,
        search: String? = null,
    ): LiveData<PagingData<AllKendaraan>>
}