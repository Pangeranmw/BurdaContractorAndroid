package com.android.burdacontractor.core.domain.repository

import com.android.burdacontractor.core.data.Resource
import com.android.burdacontractor.core.domain.model.LogisticCoordinate
import kotlinx.coroutines.flow.Flow

interface ILogisticRepository {
    suspend fun getCoordinate(logisticId: String): Flow<Resource<LogisticCoordinate>>
    suspend fun setCoordinate(logisticId: String, logisticCoordinate: LogisticCoordinate)
}