package com.android.burdacontractor.feature.kendaraan.data.source.remote.network

import com.android.burdacontractor.core.data.source.remote.response.ErrorMessageResponse
import com.android.burdacontractor.core.data.source.remote.response.ErrorMessageWithIdResponse
import com.android.burdacontractor.feature.deliveryorder.data.source.remote.response.AllDeliveryOrderResponse
import com.android.burdacontractor.feature.kendaraan.data.source.remote.response.GetAllKendaraanResponse
import com.android.burdacontractor.feature.kendaraan.data.source.remote.response.GetKendaraanByIdResponse
import com.android.burdacontractor.feature.kendaraan.data.source.remote.response.GetKendaraanByLogisticResponse
import com.android.burdacontractor.feature.kendaraan.data.source.remote.response.GetKendaraanGudangResponse
import com.android.burdacontractor.feature.suratjalan.data.source.remote.response.AllSuratJalanResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
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

    @Headers("Content-Type: application/json", "Accept: application/json")
    @GET("kendaraan/{id}/active/surat-jalan")
    suspend fun getActiveSuratJalanByKendaraan(
        @Header("Authorization") token: String,
        @Path("id") id: String,
        @Query("tipe") tipe: String,
    ): AllSuratJalanResponse

    @Headers("Content-Type: application/json", "Accept: application/json")
    @GET("kendaraan/{id}/active/delivery-order")
    suspend fun getActiveDeliveryOrderByKendaraan(
        @Header("Authorization") token: String,
        @Path("id") id: String,
    ): AllDeliveryOrderResponse

    @Headers("Content-Type: application/json", "Accept: application/json")
    @DELETE("kendaraan/{id}")
    suspend fun deleteKendaraan(
        @Header("Authorization") token: String,
        @Path("id") id: String,
    ): ErrorMessageResponse

    @Headers("Content-Type: application/json", "Accept: application/json")
    @POST("kendaraan/{id}/cancel-return")
    suspend fun cancelReturnKendaraan(
        @Header("Authorization") token: String,
        @Path("id") id: String,
    ): ErrorMessageResponse

    @Headers("Content-Type: application/json", "Accept: application/json")
    @POST("kendaraan/{id}/return")
    suspend fun returnKendaraan(
        @Header("Authorization") token: String,
        @Path("id") id: String,
    ): ErrorMessageResponse

    @Headers("Content-Type: application/json", "Accept: application/json")
    @DELETE("kendaraan/{id}/pengendara")
    suspend fun deletePengendara(
        @Header("Authorization") token: String,
        @Path("id") id: String,
    ): ErrorMessageResponse

    @Multipart
    @POST("kendaraan")
    suspend fun addKendaraan(
        @Header("Authorization") token: String,
        @Part("gudang_id") gudangId: RequestBody,
        @Part("jenis") jenis: RequestBody,
        @Part("merk") merk: RequestBody,
        @Part("plat_nomor") platNomor: RequestBody,
        @Part gambar: MultipartBody.Part,
    ): ErrorMessageWithIdResponse

    @Multipart
    @POST("kendaraan/update")
    suspend fun updateKendaraan(
        @Header("Authorization") token: String,
        @Part("id") id: RequestBody,
        @Part("gudang_id") gudangId: RequestBody,
        @Part("jenis") jenis: RequestBody,
        @Part("merk") merk: RequestBody,
        @Part("plat_nomor") platNomor: RequestBody,
        @Part gambar: MultipartBody.Part? = null,
    ): ErrorMessageWithIdResponse


    @Headers("Content-Type: application/json", "Accept: application/json")
    @GET("kendaraans")
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
    @GET("kendaraan/filter/gudang")
    suspend fun getKendaraanGudang(
        @Header("Authorization") token: String,
    ): GetKendaraanGudangResponse
}