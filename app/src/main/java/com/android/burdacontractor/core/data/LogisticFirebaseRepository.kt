package com.android.burdacontractor.core.data

import com.android.burdacontractor.core.data.source.remote.LogisticRemoteDataSource
import com.android.burdacontractor.core.domain.model.LogisticCoordinate
import com.android.burdacontractor.core.domain.repository.ILogisticFirebaseRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LogisticFirebaseRepository @Inject constructor(
    private val logisticRemoteDataSource: LogisticRemoteDataSource,
) : ILogisticFirebaseRepository {

    override fun getCoordinate(logisticId: String) =
        logisticRemoteDataSource.getCoordinate(logisticId)

    override fun getTracking(logisticId: String) = logisticRemoteDataSource.getTracking(logisticId)

    override fun setCoordinate(logisticId: String, logisticCoordinate: LogisticCoordinate) {
        logisticRemoteDataSource.setCoordinate(logisticId, logisticCoordinate)
    }

    override fun setIsTracking(logisticId: String, isTracking: Boolean) {
        logisticRemoteDataSource.setIsTracking(logisticId, isTracking)
    }

}

