package com.android.burdacontractor.feature.logistic.domain.usecase

import com.android.burdacontractor.core.data.Resource
import com.android.burdacontractor.feature.logistic.domain.model.AllLogistic
import kotlinx.coroutines.flow.Flow

interface GetAllLogisticNoPagingUseCase {
    suspend fun execute(): Flow<Resource<List<AllLogistic>>>
}
