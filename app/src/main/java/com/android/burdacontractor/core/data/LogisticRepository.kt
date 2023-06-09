package com.android.burdacontractor.core.data

import com.android.burdacontractor.core.data.source.remote.LogisticRemoteDataSource
import com.android.burdacontractor.core.domain.model.LogisticCoordinate
import com.android.burdacontractor.core.domain.repository.ILogisticRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LogisticRepository @Inject constructor(
    private val logisticRemoteDataSource: LogisticRemoteDataSource,
//    private val userRemoteDataSource: UserRemoteDataSource,
) : ILogisticRepository {

    override fun getCoordinate(logisticId: String) = logisticRemoteDataSource.getCoordinate(logisticId)

    override fun setCoordinate(logisticId: String, logisticCoordinate: LogisticCoordinate) {
        logisticRemoteDataSource.setCoordinate(logisticId, logisticCoordinate)
//        userRemoteDataSource.setCoordinate(logisticId, logisticCoordinate)
    }

}

