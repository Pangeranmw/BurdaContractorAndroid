package com.android.burdacontractor.feature.deliveryorder.domain.usecase

import com.android.burdacontractor.core.data.Resource
import com.android.burdacontractor.core.data.source.remote.response.ErrorMessageResponse
import kotlinx.coroutines.flow.Flow

interface SendDeliveryOrderUseCase {
    suspend fun execute(
        deliveryOrderId: String,
    ): Flow<Resource<ErrorMessageResponse>>
}