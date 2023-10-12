package com.android.burdacontractor.core.domain.repository

import com.android.burdacontractor.core.data.Resource
import com.android.burdacontractor.core.data.source.remote.response.DistanceMatrixResponse
import kotlinx.coroutines.flow.Flow

interface IOSRMRepository {
    suspend fun getDistanceMatrix(
        coordinates: String
    ): Flow<Resource<DistanceMatrixResponse>>
}