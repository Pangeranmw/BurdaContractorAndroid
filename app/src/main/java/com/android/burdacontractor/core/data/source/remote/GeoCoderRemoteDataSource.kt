package com.android.burdacontractor.core.data.source.remote

import com.android.burdacontractor.core.data.source.remote.network.ApiResponse
import com.android.burdacontractor.core.data.source.remote.network.GeoCoderService
import com.android.burdacontractor.core.data.source.remote.response.GetLocationFromAddressResponse
import com.android.burdacontractor.core.data.source.remote.response.GetLocationFromCoordinateResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GeoCoderRemoteDataSource @Inject constructor(
    private val geoCoderService: GeoCoderService,
) {
    suspend fun getLocationFromAddress(
        query: String,
    ): Flow<ApiResponse<GetLocationFromAddressResponse>> = flow {
        val response = geoCoderService.getLocationFromAddress(query)
        emit(ApiResponse.Success(response))
    }

    suspend fun getLocationFromCoordinate(
        lat: String,
        lon: String,
    ): Flow<ApiResponse<GetLocationFromCoordinateResponse>> = flow {
        val response = geoCoderService.getLocationFromCoordinate(lat, lon)
        emit(ApiResponse.Success(response))
    }
}

