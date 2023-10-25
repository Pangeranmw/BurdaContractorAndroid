package com.android.burdacontractor.feature.gudang.data.source.remote

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.android.burdacontractor.core.data.source.remote.network.ApiResponse
import com.android.burdacontractor.feature.gudang.data.source.remote.network.GudangService
import com.android.burdacontractor.feature.gudang.data.source.remote.response.GetGudangByIdResponse
import com.android.burdacontractor.feature.gudang.data.source.remote.response.GetGudangProvinsiResponse
import com.android.burdacontractor.feature.gudang.domain.model.AllGudang
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GudangRemoteDataSource @Inject constructor(
    private val gudangService: GudangService,
) {
    suspend fun getGudangById(
        token: String,
        id: String
    ): Flow<ApiResponse<GetGudangByIdResponse>> = flow {
        val response = gudangService.getGudangById(token, id)
        if (!response.error) {
            emit(ApiResponse.Success(response))
        } else {
            emit(ApiResponse.Error(response.message))
        }
    }

    suspend fun getGudangProvinsi(
        token: String
    ): Flow<ApiResponse<GetGudangProvinsiResponse>> = flow {
        val response = gudangService.getGudangProvinsi(token)
        if (!response.error) {
            emit(ApiResponse.Success(response))
        } else {
            emit(ApiResponse.Error(response.message))
        }
    }

    fun getAllGudang(
        token: String,
        size: Int = 5,
        search: String? = null,
        filter: String? = null,
        coordinate: String? = null,
    ): Flow<PagingData<AllGudang>> {
        return Pager(
            config = PagingConfig(
                pageSize = size
            ),
            pagingSourceFactory = {
                GudangPagingSource(gudangService, token, size, search, filter, coordinate)
            }
        ).flow
    }
}

