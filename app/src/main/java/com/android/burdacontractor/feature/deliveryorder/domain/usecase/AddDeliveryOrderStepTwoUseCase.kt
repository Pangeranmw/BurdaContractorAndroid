package com.android.burdacontractor.feature.deliveryorder.domain.usecase

import com.android.burdacontractor.core.data.Resource
import com.android.burdacontractor.feature.deliveryorder.data.source.remote.request.AddDeliveryOrderStepTwoBody
import kotlinx.coroutines.flow.Flow

interface AddDeliveryOrderStepTwoUseCase {
    suspend fun execute(
        addDeliveryOrderStepTwoBody: AddDeliveryOrderStepTwoBody
    ): Flow<Resource<String>>
}