package com.android.burdacontractor.feature.gudang.data.source.remote

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.android.burdacontractor.core.data.source.remote.network.ApiResponse
import com.android.burdacontractor.core.data.source.remote.response.ErrorMessageResponse
import com.android.burdacontractor.core.data.source.remote.response.ErrorMessageWithIdResponse
import com.android.burdacontractor.feature.deliveryorder.data.source.remote.response.AllDeliveryOrderResponse
import com.android.burdacontractor.feature.gudang.data.source.remote.network.GudangService
import com.android.burdacontractor.feature.gudang.data.source.remote.response.GetGudangByIdResponse
import com.android.burdacontractor.feature.gudang.data.source.remote.response.GetGudangProvinsiResponse
import com.android.burdacontractor.feature.gudang.domain.model.AllGudang
import com.android.burdacontractor.feature.suratjalan.data.source.remote.response.AllSuratJalanResponse
import com.android.burdacontractor.feature.suratjalan.data.source.remote.response.StatisticCountTitleResponse
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

    suspend fun getStatistikDeliveryOrderByGudang(
        token: String, id: String
    ): Flow<ApiResponse<StatisticCountTitleResponse>> = flow {
        val response = gudangService.getStatistikDeliveryOrderByGudang(token, id)
        if (!response.error) {
            emit(ApiResponse.Success(response))
        } else {
            emit(ApiResponse.Error(response.message))
        }
    }

    suspend fun getActiveDeliveryOrderByGudang(
        token: String, id: String
    ): Flow<ApiResponse<AllDeliveryOrderResponse>> = flow {
        val response = gudangService.getActiveDeliveryOrderByGudang(token, id)
        if (!response.error) {
            emit(ApiResponse.Success(response))
        } else {
            emit(ApiResponse.Error(response.message))
        }
    }

    suspend fun getStatistikSuratJalanByGudang(
        token: String, id: String, tipe: String
    ): Flow<ApiResponse<StatisticCountTitleResponse>> = flow {
        val response = gudangService.getStatistikSuratJalanByGudang(token, id, tipe)
        if (!response.error) {
            emit(ApiResponse.Success(response))
        } else {
            emit(ApiResponse.Error(response.message))
        }
    }

    suspend fun getActiveSuratJalanByGudang(
        token: String, id: String, tipe: String
    ): Flow<ApiResponse<AllSuratJalanResponse>> = flow {
        val response = gudangService.getActiveSuratJalanByGudang(token, id, tipe)
        if (!response.error) {
            emit(ApiResponse.Success(response))
        } else {
            emit(ApiResponse.Error(response.message))
        }
    }

    suspend fun deleteGudang(
        token: String, id: String
    ): Flow<ApiResponse<ErrorMessageResponse>> = flow {
        val response = gudangService.deleteGudang(token, id)
        if (!response.error) {
            emit(ApiResponse.Success(response))
        } else {
            emit(ApiResponse.Error(response.message))
        }
    }

    suspend fun addGudang(
        token: String,
        nama: String,
        alamat: String,
        provinsi: String,
        kota: String,
        latitude: String,
        longitude: String,
        gambar: File,
    ): Flow<ApiResponse<ErrorMessageWithIdResponse>> = flow {
        val namaRB = nama.toRequestBody("text/plain".toMediaType())
        val alamatRB = alamat.toRequestBody("text/plain".toMediaType())
        val provinsiRB = provinsi.toRequestBody("text/plain".toMediaType())
        val kotaRB = kota.toRequestBody("text/plain".toMediaType())
        val latitudeRB = latitude.toRequestBody("text/plain".toMediaType())
        val longitudeRB = longitude.toRequestBody("text/plain".toMediaType())
        val requestImageFile = gambar.asRequestBody("image/jpeg".toMediaType())
        val multipartBody = MultipartBody.Part.createFormData(
            "gambar",
            gambar.name,
            requestImageFile
        )
        val response = gudangService.addGudang(
            token,
            namaRB,
            alamatRB,
            provinsiRB,
            kotaRB,
            latitudeRB,
            longitudeRB,
            multipartBody
        )
        if (!response.error) {
            emit(ApiResponse.Success(response))
        } else {
            emit(ApiResponse.Error(response.message))
        }
    }

    suspend fun updateGudang(
        token: String,
        id: String,
        nama: String,
        alamat: String,
        provinsi: String,
        kota: String,
        latitude: String,
        longitude: String,
        gambar: File?,
    ): Flow<ApiResponse<ErrorMessageWithIdResponse>> = flow {
        val idRB = id.toRequestBody("text/plain".toMediaType())
        val namaRB = nama.toRequestBody("text/plain".toMediaType())
        val alamatRB = alamat.toRequestBody("text/plain".toMediaType())
        val provinsiRB = provinsi.toRequestBody("text/plain".toMediaType())
        val kotaRB = kota.toRequestBody("text/plain".toMediaType())
        val latitudeRB = latitude.toRequestBody("text/plain".toMediaType())
        val longitudeRB = longitude.toRequestBody("text/plain".toMediaType())
        var multipartBody: MultipartBody.Part? = null
        if (gambar != null) {
            val requestImageFile = gambar.asRequestBody("image/jpeg".toMediaType())
            multipartBody = MultipartBody.Part.createFormData(
                "gambar",
                gambar.name,
                requestImageFile
            )
        }
        val response = gudangService.updateGudang(
            token,
            idRB,
            namaRB,
            alamatRB,
            provinsiRB,
            kotaRB,
            latitudeRB,
            longitudeRB,
            multipartBody
        )
        if (!response.error) {
            emit(ApiResponse.Success(response))
        } else {
            emit(ApiResponse.Error(response.message))
        }
    }
}

