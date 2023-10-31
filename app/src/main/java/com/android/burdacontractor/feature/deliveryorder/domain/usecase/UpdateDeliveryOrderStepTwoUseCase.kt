package com.android.burdacontractor.feature.deliveryorder.domain.usecase

import com.android.burdacontractor.core.data.Resource
import com.android.burdacontractor.feature.deliveryorder.data.source.remote.request.AddUpdateDeliveryOrderStepTwoBody
import kotlinx.coroutines.flow.Flow

interface UpdateDeliveryOrderStepTwoUseCase {
    suspend fun execute(
        id: String,
        addUpdateDeliveryOrderStepTwoBody: AddUpdateDeliveryOrderStepTwoBody
    ): Flow<Resource<String>>
}