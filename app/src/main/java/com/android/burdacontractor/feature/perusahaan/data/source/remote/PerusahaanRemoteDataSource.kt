package com.android.burdacontractor.feature.perusahaan.data.source.remote

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.android.burdacontractor.core.data.source.remote.network.ApiResponse
import com.android.burdacontractor.core.data.source.remote.response.ErrorMessageResponse
import com.android.burdacontractor.core.data.source.remote.response.ErrorMessageWithIdResponse
import com.android.burdacontractor.feature.deliveryorder.data.source.remote.response.AllDeliveryOrderResponse
import com.android.burdacontractor.feature.perusahaan.data.source.remote.network.PerusahaanService
import com.android.burdacontractor.feature.perusahaan.data.source.remote.response.GetPerusahaanByIdResponse
import com.android.burdacontractor.feature.perusahaan.data.source.remote.response.GetPerusahaanProvinsiResponse
import com.android.burdacontractor.feature.perusahaan.domain.model.AllPerusahaan
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

    suspend fun getStatistikDeliveryOrderByPerusahaan(
        token: String, id: String
    ): Flow<ApiResponse<StatisticCountTitleResponse>> = flow {
        val response = perusahaanService.getStatistikDeliveryOrderByPerusahaan(token, id)
        if (!response.error) {
            emit(ApiResponse.Success(response))
        } else {
            emit(ApiResponse.Error(response.message))
        }
    }

    suspend fun getActiveDeliveryOrderByPerusahaan(
        token: String, id: String
    ): Flow<ApiResponse<AllDeliveryOrderResponse>> = flow {
        val response = perusahaanService.getActiveDeliveryOrderByPerusahaan(token, id)
        if (!response.error) {
            emit(ApiResponse.Success(response))
        } else {
            emit(ApiResponse.Error(response.message))
        }
    }

    suspend fun deletePerusahaan(
        token: String, id: String
    ): Flow<ApiResponse<ErrorMessageResponse>> = flow {
        val response = perusahaanService.deletePerusahaan(token, id)
        if (!response.error) {
            emit(ApiResponse.Success(response))
        } else {
            emit(ApiResponse.Error(response.message))
        }
    }

    suspend fun addPerusahaan(
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
        val response = perusahaanService.addPerusahaan(
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

    suspend fun updatePerusahaan(
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
        val response = perusahaanService.updatePerusahaan(
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

