package com.android.burdacontractor.feature.deliveryorder.domain.usecase

import com.android.burdacontractor.core.data.Resource
import com.android.burdacontractor.core.domain.model.CountActive
import kotlinx.coroutines.flow.Flow

interface GetCountActiveDeliveryOrderUseCase {
    suspend fun execute(): Flow<Resource<CountActive>>
}