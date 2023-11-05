package com.android.burdacontractor.feature.gudang.domain.usecase

import com.android.burdacontractor.core.data.Resource
import com.android.burdacontractor.feature.deliveryorder.domain.model.AllDeliveryOrder
import kotlinx.coroutines.flow.Flow

interface GetActiveDeliveryOrderByGudangUseCase {
    suspend fun execute(
        id: String,
    ): Flow<Resource<List<AllDeliveryOrder>>>
}