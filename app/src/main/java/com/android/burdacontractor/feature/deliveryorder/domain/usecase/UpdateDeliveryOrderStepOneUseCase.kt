package com.android.burdacontractor.feature.deliveryorder.domain.usecase

import com.android.burdacontractor.core.data.Resource
import com.android.burdacontractor.feature.deliveryorder.data.source.remote.request.AddUpdateDeliveryOrderStepOneBody
import kotlinx.coroutines.flow.Flow

interface UpdateDeliveryOrderStepOneUseCase {
    suspend fun execute(
        id: String,
        addUpdateDeliveryOrderStepOneBody: AddUpdateDeliveryOrderStepOneBody
    ): Flow<Resource<String>>
}