package com.android.burdacontractor.feature.kendaraan.domain.usecase

import com.android.burdacontractor.core.data.Resource
import com.android.burdacontractor.feature.deliveryorder.domain.model.AllDeliveryOrder
import kotlinx.coroutines.flow.Flow

interface GetActiveDeliveryOrderByKendaraanUseCase {
    suspend fun execute(
        id: String
    ): Flow<Resource<List<AllDeliveryOrder>>>
}