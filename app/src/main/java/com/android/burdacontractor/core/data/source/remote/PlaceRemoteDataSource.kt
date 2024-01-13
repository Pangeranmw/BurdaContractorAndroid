package com.android.burdacontractor.core.data.source.remote

import com.android.burdacontractor.core.data.source.remote.network.ApiResponse
import com.android.burdacontractor.core.data.source.remote.network.PlaceService
import com.android.burdacontractor.core.data.source.remote.response.GetPlaceByQueryResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PlaceRemoteDataSource @Inject constructor(
    private val placeService: PlaceService,
) {
    suspend fun getPlaceByQuery(
        query: String,
    ): Flow<ApiResponse<GetPlaceByQueryResponse>> = flow {
        val response = placeService.getPlaceByQuery(query)
        emit(ApiResponse.Success(response))
    }
}

