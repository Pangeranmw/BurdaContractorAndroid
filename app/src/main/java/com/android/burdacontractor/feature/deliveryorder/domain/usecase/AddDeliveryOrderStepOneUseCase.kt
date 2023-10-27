package com.android.burdacontractor.feature.deliveryorder.domain.usecase

import com.android.burdacontractor.core.data.Resource
import com.android.burdacontractor.feature.deliveryorder.data.source.remote.request.AddDeliveryOrderStepOneBody
import kotlinx.coroutines.flow.Flow

interface AddDeliveryOrderStepOneUseCase {
    suspend fun execute(
        addDeliveryOrderStepOneBody: AddDeliveryOrderStepOneBody
    ): Flow<Resource<String>>
}