package com.android.burdacontractor.core.domain.repository

import com.android.burdacontractor.core.data.Resource
import com.android.burdacontractor.core.data.source.remote.response.GetLocationFromAddressResponseItem
import com.android.burdacontractor.core.data.source.remote.response.GetLocationFromCoordinateResponse
import kotlinx.coroutines.flow.Flow

interface IGeoCoderRepository {
    suspend fun getLocationFromCoordinate(
        latitude: String,
        longitude: String,
    ): Flow<Resource<GetLocationFromCoordinateResponse>>

    suspend fun getLocationFromAddress(
        query: String,
    ): Flow<Resource<List<GetLocationFromAddressResponseItem>>>
}