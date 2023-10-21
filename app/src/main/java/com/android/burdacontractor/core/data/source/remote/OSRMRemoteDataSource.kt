package com.android.burdacontractor.core.data.source.remote

import com.android.burdacontractor.core.data.source.remote.network.ApiResponse
import com.android.burdacontractor.core.data.source.remote.network.DistanceMatrixService
import com.android.burdacontractor.core.data.source.remote.response.DistanceMatrixResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OSRMRemoteDataSource @Inject constructor(
    private val distanceMatrixService: DistanceMatrixService,
) {
    suspend fun getDistanceMatrix(
        coordinates: String,
    ): Flow<ApiResponse<DistanceMatrixResponse>> = flow {
        val response = distanceMatrixService.getDistanceMatrixOSRM(coordinates)
        if(response.code == "Ok"){
            emit(ApiResponse.Success(response))
        }else{
            emit(ApiResponse.Error(response.message))
        }
    }
}

