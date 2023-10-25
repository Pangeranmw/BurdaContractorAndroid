package com.android.burdacontractor.core.data

import com.android.burdacontractor.core.data.source.remote.LogisticFirebaseRemoteDataSource
import com.android.burdacontractor.core.domain.model.LogisticCoordinate
import com.android.burdacontractor.core.domain.repository.ILogisticFirebaseRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LogisticFirebaseRepository @Inject constructor(
    private val logisticFirebaseRemoteDataSource: LogisticFirebaseRemoteDataSource,
) : ILogisticFirebaseRepository {

    override fun getCoordinate(logisticId: String) =
        logisticFirebaseRemoteDataSource.getCoordinate(logisticId)

    override fun getTracking(logisticId: String) =
        logisticFirebaseRemoteDataSource.getTracking(logisticId)

    override fun setCoordinate(logisticId: String, logisticCoordinate: LogisticCoordinate) {
        logisticFirebaseRemoteDataSource.setCoordinate(logisticId, logisticCoordinate)
    }

    override fun setIsTracking(logisticId: String, isTracking: Boolean) {
        logisticFirebaseRemoteDataSource.setIsTracking(logisticId, isTracking)
    }

}

