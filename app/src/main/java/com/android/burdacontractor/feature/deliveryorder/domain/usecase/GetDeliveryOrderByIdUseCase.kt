package com.android.burdacontractor.feature.deliveryorder.domain.usecase

import com.android.burdacontractor.core.data.Resource
import com.android.burdacontractor.feature.deliveryorder.domain.model.DeliveryOrderDetailItem
import kotlinx.coroutines.flow.Flow

interface GetDeliveryOrderByIdUseCase {
    suspend fun execute(id: String): Flow<Resource<DeliveryOrderDetailItem>>
}