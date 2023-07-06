package com.android.burdacontractor.feature.kendaraan.data.source.remote

import com.android.burdacontractor.core.data.source.remote.network.ApiResponse
import com.android.burdacontractor.feature.kendaraan.data.source.remote.network.KendaraanService
import com.android.burdacontractor.feature.kendaraan.data.source.remote.response.GetKendaraanByLogisticResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class KendaraanRemoteDataSource @Inject constructor(
    private val kendaraanService: KendaraanService,
) {
    suspend fun getKendaraanByLogistic(
        token: String
    ): Flow<ApiResponse<GetKendaraanByLogisticResponse>> = flow {
        val response = kendaraanService.getKendaraanByLogistic(token)
        if(!response.error){
            emit(ApiResponse.Success(response))
        }else{
            emit(ApiResponse.Error(response.message))
        }
    }
}

