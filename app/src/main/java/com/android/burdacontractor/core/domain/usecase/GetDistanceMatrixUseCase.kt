package com.android.burdacontractor.core.domain.usecase

import com.android.burdacontractor.core.data.Resource
import com.android.burdacontractor.core.data.source.remote.response.DistanceMatrixResponse
import com.android.burdacontractor.core.domain.model.LogisticCoordinate
import kotlinx.coroutines.flow.Flow

interface GetDistanceMatrixUseCase {
    suspend fun execute(coordinates: String): Flow<Resource<DistanceMatrixResponse>>
}