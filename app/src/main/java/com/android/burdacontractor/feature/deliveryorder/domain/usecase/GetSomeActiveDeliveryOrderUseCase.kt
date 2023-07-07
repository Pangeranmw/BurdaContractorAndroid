package com.android.burdacontractor.feature.deliveryorder.domain.usecase

import com.android.burdacontractor.core.data.Resource
import com.android.burdacontractor.feature.deliveryorder.domain.model.DataAllDeliveryOrderWithCount
import kotlinx.coroutines.flow.Flow

interface GetSomeActiveDeliveryOrderUseCase {
    suspend fun execute(size: Int = 5): Flow<Resource<DataAllDeliveryOrderWithCount>>
}