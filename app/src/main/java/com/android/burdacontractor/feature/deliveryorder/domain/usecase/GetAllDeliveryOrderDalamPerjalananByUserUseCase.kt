package com.android.burdacontractor.feature.deliveryorder.domain.usecase

import com.android.burdacontractor.core.data.Resource
import com.android.burdacontractor.feature.deliveryorder.domain.model.AllDeliveryOrder
import kotlinx.coroutines.flow.Flow

interface GetAllDeliveryOrderDalamPerjalananByUserUseCase {
    suspend fun execute(): Flow<Resource<List<AllDeliveryOrder>>>
}