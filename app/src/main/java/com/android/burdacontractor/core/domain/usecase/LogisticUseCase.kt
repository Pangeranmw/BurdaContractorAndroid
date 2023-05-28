package com.android.burdacontractor.core.domain.usecase

import com.android.burdacontractor.core.data.Resource
import com.android.burdacontractor.core.domain.model.LogisticCoordinate
import kotlinx.coroutines.flow.Flow

interface LogisticUseCase {
    fun getCoordinate(logisticId: String): Flow<Resource<LogisticCoordinate>>
    fun setCoordinate(logisticId: String, logisticCoordinate: LogisticCoordinate)
}