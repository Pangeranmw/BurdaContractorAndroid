package com.android.burdacontractor.feature.suratjalan.data.source.remote

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.android.burdacontractor.core.data.Resource
import com.android.burdacontractor.core.data.source.remote.network.ApiResponse
import com.android.burdacontractor.core.data.source.remote.response.CountActiveResponse
import com.android.burdacontractor.core.data.source.remote.response.ErrorMessageResponse
import com.android.burdacontractor.core.domain.model.enums.SuratJalanStatus
import com.android.burdacontractor.core.domain.model.enums.SuratJalanTipe
import com.android.burdacontractor.feature.suratjalan.data.source.remote.network.SuratJalanService
import com.android.burdacontractor.feature.suratjalan.data.source.remote.response.AllSuratJalanResponse
import com.android.burdacontractor.feature.suratjalan.data.source.remote.response.AllSuratJalanWithCountResponse
import com.android.burdacontractor.feature.suratjalan.data.source.remote.response.StatisticCountTitleResponse
import com.android.burdacontractor.feature.suratjalan.data.source.remote.response.SuratJalanDetailResponse
import com.android.burdacontractor.feature.suratjalan.domain.model.AllSuratJalan
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SuratJalanRemoteDataSource @Inject constructor(
    private val suratJalanService: SuratJalanService,
) {
    fun getAllSuratJalan(
        token: String,
        tipe: SuratJalanTipe,
        status: SuratJalanStatus,
        date_start: String? = null,
        date_end: String? = null,
        size: Int = 5,
        search: String? = null,
    ): Flow<PagingData<AllSuratJalan>> {
        return Pager(
            config = PagingConfig(
                pageSize = size
            ),
            pagingSourceFactory = {
                SuratJalanPagingSource(
                    suratJalanService,
                    token,
                    tipe.name,
                    status.name,
                    date_start,
                    date_end,
                    size,
                    search
                )
            }
        ).flow
    }

    suspend fun getAllSuratJalanWithCount(
        token: String,
        tipe: SuratJalanTipe,
        size: Int = 5,
    ): Flow<ApiResponse<AllSuratJalanWithCountResponse>> = flow {
        val response = suratJalanService.getSomeActiveSuratJalan(token, tipe.name, size)
        if(!response.error){
            emit(ApiResponse.Success(response))
        }else{
            emit(ApiResponse.Error(response.message))
        }
    }
    suspend fun getStatistikMenungguSuratJalan(
        token: String,
    ): Flow<ApiResponse<StatisticCountTitleResponse>> = flow {
        val response = suratJalanService.getStatistikMenungguSuratJalan(token)
        if(!response.error){
            emit(ApiResponse.Success(response))
        }else{
            emit(ApiResponse.Error(response.message))
        }
    }

    suspend fun getCountActiveSuratJalan(
        token: String,
    ): Flow<ApiResponse<CountActiveResponse>> = flow {
        val response = suratJalanService.getCountActiveSuratJalan(token)
        if(!response.error){
            emit(ApiResponse.Success(response))
        }else{
            emit(ApiResponse.Error(response.message))
        }
    }

    suspend fun getAllSuratJalanDalamPerjalananByUser(
        token: String
    ): Flow<ApiResponse<AllSuratJalanResponse>> = flow {
        val response = suratJalanService.getAllSuratJalanDalamPerjalananByUser(token)
        if(!response.error){
            emit(ApiResponse.Success(response))
        }else{
            emit(ApiResponse.Error(response.message))
        }
    }

    suspend fun getSuratJalanById(token:String, id: String): Flow<ApiResponse<SuratJalanDetailResponse>> = flow{
        val response = suratJalanService.getSuratJalanById(token, id)
        if(!response.error){
            emit(ApiResponse.Success(response))
        }else{
            emit(ApiResponse.Error(response.message))
        }
    }

    suspend fun addSuratJalanPengirimanGp(
        adminGudangId: String,
        logisticId: String,
        kendaraanId: String,
        peminjamanId: String
    ): Flow<Resource<ErrorMessageResponse>> = flow{

    }

    suspend fun addSuratJalanPengirimanPp(
        adminGudangId: String,
        logisticId: String,
        kendaraanId: String,
        peminjamanAsalId: String,
        peminjamanTujuanId: String,
    ): Flow<Resource<ErrorMessageResponse>> = flow{

    }

    suspend fun addSuratJalanPengembalian(
        adminGudangId: String,
        logisticId: String,
        kendaraanId: String,
        pengembalianId: String,
    ): Flow<Resource<ErrorMessageResponse>> = flow {

    }

    suspend fun updateSuratJalanPengirimanGp(
        adminGudangId: String,
        logisticId: String,
        kendaraanId: String,
        peminjamanId: String
    ): Flow<Resource<ErrorMessageResponse>> = flow{

    }

    suspend fun updateSuratJalanPengirimanGp(
        adminGudangId: String,
        logisticId: String,
        kendaraanId: String,
        peminjamanAsalId: String,
        peminjamanTujuanId: String,
    ): Flow<Resource<ErrorMessageResponse>> = flow{

    }

    suspend fun updateSuratJalanPengembalian(
        adminGudangId: String,
        logisticId: String,
        kendaraanId: String,
        pengembalianId: String,
    ): Flow<Resource<ErrorMessageResponse>> = flow{

    }

    suspend fun deleteSuratJalan(
        suratJalanId: String,
    ): Flow<Resource<ErrorMessageResponse>> = flow{

    }
}

