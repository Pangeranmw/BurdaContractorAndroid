package com.android.burdacontractor.feature.deliveryorder.domain.usecase

import com.android.burdacontractor.core.data.Resource
import com.android.burdacontractor.core.data.source.remote.response.ErrorMessageResponse
import kotlinx.coroutines.flow.Flow

interface DeletePreOrderUseCase {
    suspend fun execute(
        preOrderId: String,
    ): Flow<Resource<ErrorMessageResponse>>
}