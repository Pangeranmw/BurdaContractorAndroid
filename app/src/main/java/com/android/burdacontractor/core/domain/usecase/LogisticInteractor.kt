package com.android.burdacontractor.core.domain.usecase

import com.android.burdacontractor.core.data.LogisticRepository
import com.android.burdacontractor.core.data.Resource
import com.android.burdacontractor.core.domain.model.LogisticCoordinate
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LogisticInteractor @Inject constructor(private val logisticRepository: LogisticRepository):
    LogisticUseCase {

    override suspend fun setCoordinate(logisticId: String, logisticCoordinate: LogisticCoordinate) {
        logisticRepository.setCoordinate(logisticId, logisticCoordinate)
    }
    override suspend fun getCoordinate(logisticId: String): Flow<Resource<LogisticCoordinate>> = logisticRepository.getCoordinate(logisticId)

}