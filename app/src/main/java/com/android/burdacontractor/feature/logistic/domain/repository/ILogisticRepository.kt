package com.android.burdacontractor.feature.logistic.domain.repository

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.android.burdacontractor.core.data.Resource
import com.android.burdacontractor.feature.logistic.domain.model.ActiveSjDoLocation
import com.android.burdacontractor.feature.logistic.domain.model.AllLogistic
import com.android.burdacontractor.feature.logistic.domain.model.LogisticById
import kotlinx.coroutines.flow.Flow

interface ILogisticRepository {
    fun getAllLogistic(
        search: String? = null,
        coordinate: String? = null,
        size: Int = 5
    ): LiveData<PagingData<AllLogistic>>

    suspend fun getLogisticById(
        id: String
    ): Flow<Resource<LogisticById>>

    suspend fun getLogisticActiveSjDoLocation(
        id: String
    ): Flow<Resource<List<ActiveSjDoLocation>>>
}