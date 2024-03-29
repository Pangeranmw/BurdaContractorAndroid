package com.android.burdacontractor.feature.deliveryorder.domain.usecase

import com.android.burdacontractor.core.data.Resource
import com.android.burdacontractor.feature.deliveryorder.data.source.remote.request.AddUpdateDeliveryOrderStepTwoBody
import kotlinx.coroutines.flow.Flow

interface AddDeliveryOrderStepTwoUseCase {
    suspend fun execute(
        addUpdateDeliveryOrderStepTwoBody: AddUpdateDeliveryOrderStepTwoBody
    ): Flow<Resource<String>>
}