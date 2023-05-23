package com.android.burdacontractor.core.domain.usecase

import com.android.burdacontractor.core.data.Resource
import com.android.burdacontractor.core.domain.model.LogisticCoordinate
import kotlinx.coroutines.flow.Flow

interface LogisticUseCase {
    suspend fun getCoordinate(logisticId: String): Flow<Resource<LogisticCoordinate>>
    suspend fun setCoordinate(logisticId: String, logisticCoordinate: LogisticCoordinate)
}