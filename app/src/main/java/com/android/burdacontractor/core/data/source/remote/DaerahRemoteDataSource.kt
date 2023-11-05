package com.android.burdacontractor.core.data.source.remote

import com.android.burdacontractor.core.data.source.remote.network.ApiResponse
import com.android.burdacontractor.core.data.source.remote.network.DaerahService
import com.android.burdacontractor.core.data.source.remote.response.GetCityByProvinceResponse
import com.android.burdacontractor.core.data.source.remote.response.GetProvinceResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DaerahRemoteDataSource @Inject constructor(
    private val daerahService: DaerahService,
) {
    suspend fun getProvince(): Flow<ApiResponse<GetProvinceResponse>> = flow {
        val response = daerahService.getProvince()
        if (!response.error) {
            emit(ApiResponse.Success(response))
        } else {
            emit(ApiResponse.Error(response.message))
        }
    }

    suspend fun getCityByProvince(province: String): Flow<ApiResponse<GetCityByProvinceResponse>> =
        flow {
            val response = daerahService.getCityByProvince(province)
            if (!response.error) {
                emit(ApiResponse.Success(response))
            } else {
                emit(ApiResponse.Error(response.message))
            }
        }
}

