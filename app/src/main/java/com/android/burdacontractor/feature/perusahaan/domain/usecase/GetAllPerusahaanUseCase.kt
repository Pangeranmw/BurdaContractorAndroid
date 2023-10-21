package com.android.burdacontractor.feature.perusahaan.domain.usecase

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.android.burdacontractor.feature.perusahaan.domain.model.AllPerusahaan

interface GetAllPerusahaanUseCase {
    fun execute(
        size: Int = 5,
        search: String? = null,
        filter: String? = null,
        coordinate: String? = null,
    ): LiveData<PagingData<AllPerusahaan>>
}