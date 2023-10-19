package com.android.burdacontractor.feature.deliveryorder.domain.usecase

import com.android.burdacontractor.core.data.Resource
import com.android.burdacontractor.feature.deliveryorder.data.source.remote.response.DataAllDeliveryOrderWithCountItem
import kotlinx.coroutines.flow.Flow

interface GetSomeActiveDeliveryOrderUseCase {
    suspend fun execute(size: Int = 5): Flow<Resource<DataAllDeliveryOrderWithCountItem>>
}