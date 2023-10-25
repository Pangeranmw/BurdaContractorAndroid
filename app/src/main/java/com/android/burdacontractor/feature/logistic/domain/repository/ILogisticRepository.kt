package com.android.burdacontractor.feature.logistic.domain.repository

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.android.burdacontractor.feature.logistic.domain.model.AllLogistic

interface ILogisticRepository {
    fun getAllLogistic(
        search: String? = null,
        coordinate: String? = null,
        size: Int = 5
    ): LiveData<PagingData<AllLogistic>>
}