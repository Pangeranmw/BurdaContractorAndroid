package com.android.burdacontractor.feature.perusahaan.data.source.remote

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.android.burdacontractor.core.data.source.remote.network.ApiResponse
import com.android.burdacontractor.feature.perusahaan.data.source.remote.network.PerusahaanService
import com.android.burdacontractor.feature.perusahaan.data.source.remote.response.GetPerusahaanByIdResponse
import com.android.burdacontractor.feature.perusahaan.data.source.remote.response.GetPerusahaanProvinsiResponse
import com.android.burdacontractor.feature.perusahaan.domain.model.AllPerusahaan
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PerusahaanRemoteDataSource @Inject constructor(
    private val perusahaanService: PerusahaanService,
) {
    suspend fun getPerusahaanById(
        token: String,
        id: String
    ): Flow<ApiResponse<GetPerusahaanByIdResponse>> = flow {
        val response = perusahaanService.getPerusahaanById(token, id)
        if (!response.error) {
            emit(ApiResponse.Success(response))
        } else {
            emit(ApiResponse.Error(response.message))
        }
    }

    suspend fun getPerusahaanProvinsi(
        token: String
    ): Flow<ApiResponse<GetPerusahaanProvinsiResponse>> = flow {
        val response = perusahaanService.getPerusahaanProvinsi(token)
        if (!response.error) {
            emit(ApiResponse.Success(response))
        } else {
            emit(ApiResponse.Error(response.message))
        }
    }

    fun getAllPerusahaan(
        token: String,
        size: Int = 5,
        search: String? = null,
        filter: String? = null,
        coordinate: String? = null,
    ): Flow<PagingData<AllPerusahaan>> {
        return Pager(
            config = PagingConfig(
                pageSize = size
            ),
            pagingSourceFactory = {
                PerusahaanPagingSource(perusahaanService, token, size, search, filter, coordinate)
            }
        ).flow
    }
}

