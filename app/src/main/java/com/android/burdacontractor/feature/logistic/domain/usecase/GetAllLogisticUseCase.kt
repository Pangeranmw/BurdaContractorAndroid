package com.android.burdacontractor.feature.logistic.domain.usecase

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.android.burdacontractor.feature.logistic.domain.model.AllLogistic

interface GetAllLogisticUseCase {
    fun execute(
        search: String? = null,
        coordinate: String? = null,
        size: Int = 5,
    ): LiveData<PagingData<AllLogistic>>
}