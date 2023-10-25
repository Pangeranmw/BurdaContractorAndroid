package com.android.burdacontractor.feature.kendaraan.data.source.remote

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.android.burdacontractor.core.data.source.remote.network.ApiResponse
import com.android.burdacontractor.feature.kendaraan.data.source.remote.network.KendaraanService
import com.android.burdacontractor.feature.kendaraan.data.source.remote.response.GetKendaraanByIdResponse
import com.android.burdacontractor.feature.kendaraan.data.source.remote.response.GetKendaraanByLogisticResponse
import com.android.burdacontractor.feature.kendaraan.data.source.remote.response.GetKendaraanGudangResponse
import com.android.burdacontractor.feature.kendaraan.domain.model.AllKendaraan
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
        if (!response.error) {
            emit(ApiResponse.Success(response))
        } else {
            emit(ApiResponse.Error(response.message))
        }
    }

    suspend fun getKendaraanById(
        token: String, id: String
    ): Flow<ApiResponse<GetKendaraanByIdResponse>> = flow {
        val response = kendaraanService.getKendaraanById(token, id)
        if (!response.error) {
            emit(ApiResponse.Success(response))
        } else {
            emit(ApiResponse.Error(response.message))
        }
    }

    suspend fun getKendaraanGudang(
        token: String
    ): Flow<ApiResponse<GetKendaraanGudangResponse>> = flow {
        val response = kendaraanService.getKendaraanGudang(token)
        if (!response.error) {
            emit(ApiResponse.Success(response))
        } else {
            emit(ApiResponse.Error(response.message))
        }
    }

    fun getAllKendaraan(
        token: String,
        size: Int = 5,
        filter: String? = null,
        gudang: String? = null,
        status: String? = null,
        search: String? = null,
    ): Flow<PagingData<AllKendaraan>> {
        return Pager(
            config = PagingConfig(
                pageSize = size
            ),
            pagingSourceFactory = {
                KendaraanPagingSource(kendaraanService, token, size, filter, gudang, status, search)
            }
        ).flow
    }
}

