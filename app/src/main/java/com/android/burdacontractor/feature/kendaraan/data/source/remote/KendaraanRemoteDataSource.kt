package com.android.burdacontractor.feature.kendaraan.data.source.remote

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.android.burdacontractor.core.data.source.remote.network.ApiResponse
import com.android.burdacontractor.core.data.source.remote.response.ErrorMessageResponse
import com.android.burdacontractor.core.data.source.remote.response.ErrorMessageWithIdResponse
import com.android.burdacontractor.core.domain.model.enums.SuratJalanTipe
import com.android.burdacontractor.feature.deliveryorder.data.source.remote.response.AllDeliveryOrderResponse
import com.android.burdacontractor.feature.kendaraan.data.source.remote.network.KendaraanService
import com.android.burdacontractor.feature.kendaraan.data.source.remote.response.GetKendaraanByIdResponse
import com.android.burdacontractor.feature.kendaraan.data.source.remote.response.GetKendaraanByLogisticResponse
import com.android.burdacontractor.feature.kendaraan.data.source.remote.response.GetKendaraanGudangResponse
import com.android.burdacontractor.feature.kendaraan.domain.model.AllKendaraan
import com.android.burdacontractor.feature.suratjalan.data.source.remote.response.AllSuratJalanResponse
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

    suspend fun cancelReturnKendaraan(
        token: String, id: String
    ): Flow<ApiResponse<ErrorMessageResponse>> = flow {
        val response = kendaraanService.cancelReturnKendaraan(token, id)
        if (!response.error) {
            emit(ApiResponse.Success(response))
        } else {
            emit(ApiResponse.Error(response.message))
        }
    }

    suspend fun returnKendaraan(
        token: String, id: String
    ): Flow<ApiResponse<ErrorMessageResponse>> = flow {
        val response = kendaraanService.returnKendaraan(token, id)
        if (!response.error) {
            emit(ApiResponse.Success(response))
        } else {
            emit(ApiResponse.Error(response.message))
        }
    }

    suspend fun deleteKendaraan(
        token: String, id: String
    ): Flow<ApiResponse<ErrorMessageResponse>> = flow {
        val response = kendaraanService.deleteKendaraan(token, id)
        if (!response.error) {
            emit(ApiResponse.Success(response))
        } else {
            emit(ApiResponse.Error(response.message))
        }
    }

    suspend fun deletePengendara(
        token: String, id: String
    ): Flow<ApiResponse<ErrorMessageResponse>> = flow {
        val response = kendaraanService.deletePengendara(token, id)
        if (!response.error) {
            emit(ApiResponse.Success(response))
        } else {
            emit(ApiResponse.Error(response.message))
        }
    }

    suspend fun getActiveDeliveryOrderByKendaraan(
        token: String, id: String
    ): Flow<ApiResponse<AllDeliveryOrderResponse>> = flow {
        val response = kendaraanService.getActiveDeliveryOrderByKendaraan(token, id)
        if (!response.error) {
            emit(ApiResponse.Success(response))
        } else {
            emit(ApiResponse.Error(response.message))
        }
    }

    suspend fun getActiveSuratJalanByKendaraan(
        token: String, id: String, tipe: SuratJalanTipe
    ): Flow<ApiResponse<AllSuratJalanResponse>> = flow {
        val response = kendaraanService.getActiveSuratJalanByKendaraan(token, id, tipe.name)
        if (!response.error) {
            emit(ApiResponse.Success(response))
        } else {
            emit(ApiResponse.Error(response.message))
        }
    }

    suspend fun addKendaraan(
        token: String,
        gudangId: String,
        jenis: String,
        merk: String,
        platNomor: String,
        gambar: File,
    ): Flow<ApiResponse<ErrorMessageWithIdResponse>> = flow {
        val gudangIdRB = gudangId.toRequestBody("text/plain".toMediaType())
        val jenisRB = jenis.toRequestBody("text/plain".toMediaType())
        val merkRB = merk.toRequestBody("text/plain".toMediaType())
        val platNomorRB = platNomor.toRequestBody("text/plain".toMediaType())
        val requestImageFile = gambar.asRequestBody("image/jpeg".toMediaType())
        val multipartBody = MultipartBody.Part.createFormData(
            "gambar",
            gambar.name,
            requestImageFile
        )
        val response = kendaraanService.addKendaraan(
            token,
            gudangIdRB,
            jenisRB,
            merkRB,
            platNomorRB,
            multipartBody
        )
        if (!response.error) {
            emit(ApiResponse.Success(response))
        } else {
            emit(ApiResponse.Error(response.message))
        }
    }

    suspend fun updateKendaraan(
        token: String,
        id: String,
        gudangId: String,
        jenis: String,
        merk: String,
        platNomor: String,
        gambar: File?,
    ): Flow<ApiResponse<ErrorMessageWithIdResponse>> = flow {
        val idRB = id.toRequestBody("text/plain".toMediaType())
        val gudangIdRB = gudangId.toRequestBody("text/plain".toMediaType())
        val jenisRB = jenis.toRequestBody("text/plain".toMediaType())
        val merkRB = merk.toRequestBody("text/plain".toMediaType())
        val platNomorRB = platNomor.toRequestBody("text/plain".toMediaType())
        var multipartBody: MultipartBody.Part? = null
        if (gambar != null) {
            val requestImageFile = gambar.asRequestBody("image/jpeg".toMediaType())
            multipartBody = MultipartBody.Part.createFormData(
                "gambar",
                gambar.name,
                requestImageFile
            )
        }
        val response = kendaraanService.updateKendaraan(
            token,
            idRB,
            gudangIdRB,
            jenisRB,
            merkRB,
            platNomorRB,
            multipartBody
        )
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

