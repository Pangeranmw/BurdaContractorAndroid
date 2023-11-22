package com.android.burdacontractor.feature.suratjalan.data.source.remote

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.android.burdacontractor.core.data.Resource
import com.android.burdacontractor.core.data.source.remote.network.ApiResponse
import com.android.burdacontractor.core.data.source.remote.response.CountActiveResponse
import com.android.burdacontractor.core.data.source.remote.response.ErrorMessageResponse
import com.android.burdacontractor.core.domain.model.enums.CreatedByOrFor
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
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
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
        dateStart: String? = null,
        dateEnd: String? = null,
        size: Int = 5,
        search: String? = null,
        createdByOrFor: CreatedByOrFor,
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
                    dateStart,
                    dateEnd,
                    size,
                    search,
                    createdByOrFor
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
    ): Flow<Resource<ErrorMessageResponse>> = flow {

    }

    suspend fun deleteSuratJalan(
        token: String,
        id: String
    ): Flow<ApiResponse<ErrorMessageResponse>> = flow {
        val response = suratJalanService.deleteSuratJalan(token, id)
        if (!response.error) {
            emit(ApiResponse.Success(response))
        } else {
            emit(ApiResponse.Error(response.message))
        }
    }

    suspend fun deleteSuratJalanChild(
        token: String,
        id: String,
        tipe: String
    ): Flow<ApiResponse<ErrorMessageResponse>> = flow {
        val response = suratJalanService.deleteSuratJalanChild(token, id, tipe)
        if (!response.error) {
            emit(ApiResponse.Success(response))
        } else {
            emit(ApiResponse.Error(response.message))
        }
    }

    suspend fun giveTtdSuratJalan(
        token: String,
        id: String
    ): Flow<ApiResponse<ErrorMessageResponse>> = flow {
        val response = suratJalanService.giveTtdSuratJalan(token, id)
        if (!response.error) {
            emit(ApiResponse.Success(response))
        } else {
            emit(ApiResponse.Error(response.message))
        }
    }

    suspend fun markCompleteSuratJalan(
        token: String,
        id: String
    ): Flow<ApiResponse<ErrorMessageResponse>> = flow {
        val response = suratJalanService.markCompleteSuratJalan(token, id)
        if (!response.error) {
            emit(ApiResponse.Success(response))
        } else {
            emit(ApiResponse.Error(response.message))
        }
    }

    suspend fun sendSuratJalan(token: String, id: String): Flow<ApiResponse<ErrorMessageResponse>> =
        flow {
            val response = suratJalanService.sendSuratJalan(token, id)
            if (!response.error) {
                emit(ApiResponse.Success(response))
            } else {
                emit(ApiResponse.Error(response.message))
            }
        }

    suspend fun uploadFotoBuktiSuratJalan(
        token: String,
        id: String,
        fotoBukti: File,
    ): Flow<ApiResponse<ErrorMessageResponse>> = flow {
        val requestBody = id.toRequestBody("text/plain".toMediaType())
        val requestImageFile = fotoBukti.asRequestBody("image/jpeg".toMediaType())
        val multipartBody = MultipartBody.Part.createFormData(
            "foto_bukti",
            fotoBukti.name,
            requestImageFile
        )
        val response =
            suratJalanService.uploadFotoBuktiSuratJalan(token, requestBody, multipartBody)
        if (!response.error) {
            emit(ApiResponse.Success(response))
        } else {
            emit(ApiResponse.Error(response.message))
        }
    }
}

