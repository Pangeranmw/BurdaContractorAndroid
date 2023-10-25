package com.android.burdacontractor.feature.kendaraan.data.source.remote.network

import com.android.burdacontractor.core.data.source.remote.response.ErrorMessageResponse
import com.android.burdacontractor.feature.kendaraan.data.source.remote.response.GetAllKendaraanResponse
import com.android.burdacontractor.feature.kendaraan.data.source.remote.response.GetKendaraanByIdResponse
import com.android.burdacontractor.feature.kendaraan.data.source.remote.response.GetKendaraanByLogisticResponse
import com.android.burdacontractor.feature.kendaraan.data.source.remote.response.GetKendaraanGudangResponse
import okhttp3.MultipartBody
import retrofit2.http.*

interface KendaraanService {
    @Headers("Content-Type: application/json", "Accept: application/json")
    @GET("kendaraan/logistic")
    suspend fun getKendaraanByLogistic(
        @Header("Authorization") token: String,
    ): GetKendaraanByLogisticResponse

    @Headers("Content-Type: application/json", "Accept: application/json")
    @GET("kendaraan/{id}")
    suspend fun getKendaraanById(
        @Header("Authorization") token: String,
        @Path("id") id: String,
    ): GetKendaraanByIdResponse

    @Multipart
    @POST("kendaraan")
    suspend fun addKendaraan(
        @Header("Authorization") token: String,
        @Part("foto") foto: MultipartBody.Part,
    ): ErrorMessageResponse

    @Headers("Content-Type: application/json", "Accept: application/json")
    @GET("kendaraan/all")
    suspend fun getAllKendaraan(
        @Header("Authorization") token: String,
        @Query("page") page: Int = 1,
        @Query("size") size: Int = 5,
        @Query("filter") filter: String? = null,
        @Query("gudang") gudang: String? = null,
        @Query("status") status: String? = null,
        @Query("search") search: String? = null,
    ): GetAllKendaraanResponse

    @Headers("Content-Type: application/json", "Accept: application/json")
    @GET("kendaraan/gudang")
    suspend fun getKendaraanGudang(
        @Header("Authorization") token: String,
    ): GetKendaraanGudangResponse
}