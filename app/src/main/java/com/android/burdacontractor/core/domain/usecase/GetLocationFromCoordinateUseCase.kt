package com.android.burdacontractor.core.domain.usecase

import com.android.burdacontractor.core.data.Resource
import com.android.burdacontractor.core.data.source.remote.response.GetLocationFromCoordinateResponse
import kotlinx.coroutines.flow.Flow

interface GetLocationFromCoordinateUseCase {
    suspend fun execute(lat: String, lon: String): Flow<Resource<GetLocationFromCoordinateResponse>>
}