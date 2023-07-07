package com.android.burdacontractor.feature.deliveryorder.domain.usecase

import com.android.burdacontractor.core.data.Resource
import com.android.burdacontractor.core.data.source.remote.response.ErrorMessageResponse
import kotlinx.coroutines.flow.Flow

interface UpdateDeliveryOrderUseCase {
    suspend fun execute(
        adminGudangId: String,
        logisticId: String,
        kendaraanId: String,
        peminjamanId: String,
    ): Flow<Resource<ErrorMessageResponse>>
}