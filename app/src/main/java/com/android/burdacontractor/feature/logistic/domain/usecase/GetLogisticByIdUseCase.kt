package com.android.burdacontractor.feature.logistic.domain.usecase

import com.android.burdacontractor.core.data.Resource
import com.android.burdacontractor.feature.logistic.domain.model.LogisticById
import kotlinx.coroutines.flow.Flow

interface GetLogisticByIdUseCase {
    suspend fun execute(id: String): Flow<Resource<LogisticById>>
}