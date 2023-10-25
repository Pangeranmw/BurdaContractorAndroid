package com.android.burdacontractor.feature.gudang.domain.usecase

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.android.burdacontractor.feature.gudang.domain.model.AllGudang

interface GetAllGudangUseCase {
    fun execute(
        size: Int = 5,
        search: String? = null,
        filter: String? = null,
        coordinate: String? = null,
    ): LiveData<PagingData<AllGudang>>
}