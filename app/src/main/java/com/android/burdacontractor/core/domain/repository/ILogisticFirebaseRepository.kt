package com.android.burdacontractor.core.domain.repository

import com.android.burdacontractor.core.data.Resource
import com.android.burdacontractor.core.domain.model.LogisticCoordinate
import com.android.burdacontractor.core.domain.model.LogisticIsTracking
import kotlinx.coroutines.flow.Flow

interface ILogisticFirebaseRepository {
    fun getCoordinate(logisticId: String): Flow<Resource<LogisticCoordinate>>
    fun getTracking(logisticId: String): Flow<Resource<LogisticIsTracking>>
    fun setCoordinate(logisticId: String, logisticCoordinate: LogisticCoordinate)
    fun setIsTracking(logisticId: String, isTracking: Boolean)
}