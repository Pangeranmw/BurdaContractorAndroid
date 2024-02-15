package com.android.burdacontractor.core.domain.usecase

import com.android.burdacontractor.core.data.Resource
import com.android.burdacontractor.core.domain.model.LogisticCoordinate
import com.android.burdacontractor.core.domain.model.LogisticIsTracking
import com.android.burdacontractor.core.domain.repository.ILogisticFirebaseRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LogisticFirebaseFirebaseInteractor @Inject constructor(private val logisticRepository: ILogisticFirebaseRepository) :
    LogisticFirebaseUseCase {
    override fun setCoordinate(logisticId: String, logisticCoordinate: LogisticCoordinate) {
        logisticRepository.setCoordinate(logisticId, logisticCoordinate)
    }

    override fun setIsTracking(logisticId: String, isTracking: Boolean) {
        logisticRepository.setIsTracking(logisticId, isTracking)
    }

    override fun getCoordinate(logisticId: String): Flow<Resource<LogisticCoordinate>> =
        logisticRepository.getCoordinate(logisticId)

    override fun getTracking(logisticId: String): Flow<Resource<LogisticIsTracking>> =
        logisticRepository.getTracking(logisticId)

}