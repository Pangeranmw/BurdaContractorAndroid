package com.android.burdacontractor.feature.gudang.data.source.remote.network

import com.android.burdacontractor.core.data.source.remote.response.ErrorMessageResponse
import com.android.burdacontractor.core.data.source.remote.response.ErrorMessageWithIdResponse
import com.android.burdacontractor.feature.deliveryorder.data.source.remote.response.AllDeliveryOrderResponse
import com.android.burdacontractor.feature.gudang.data.source.remote.response.GetAllGudangResponse
import com.android.burdacontractor.feature.gudang.data.source.remote.response.GetGudangByIdResponse
import com.android.burdacontractor.feature.gudang.data.source.remote.response.GetGudangProvinsiResponse
import com.android.burdacontractor.feature.suratjalan.data.source.remote.response.AllSuratJalanResponse
import com.android.burdacontractor.feature.suratjalan.data.source.remote.response.StatisticCountTitleResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface GudangService {
    @Headers("Content-Type: application/json", "Accept: application/json")
    @GET("gudang/{id}")
    suspend fun getGudangById(
        @Header("Authorization") token: String,
        @Path("id") id: String,
    ): GetGudangByIdResponse

    @Headers("Content-Type: application/json", "Accept: application/json")
    @GET("gudangs")
    suspend fun getAllGudang(
        @Header("Authorization") token: String,
        @Query("page") page: Int = 1,
        @Query("size") size: Int = 5,
        @Query("search") search: String? = null,
        @Query("filter") provinsi: String? = null,
        @Query("coordinate") coordinate: String? = null,
    ): GetAllGudangResponse

    @Headers("Content-Type: application/json", "Accept: application/json")
    @GET("gudang/provinsi")
    suspend fun getGudangProvinsi(
        @Header("Authorization") token: String,
    ): GetGudangProvinsiResponse

    @Headers("Content-Type: application/json", "Accept: application/json")
    @GET("gudang/{id}/statistic/delivery-order")
    suspend fun getStatistikDeliveryOrderByGudang(
        @Header("Authorization") token: String,
        @Path("id") id: String,
    ): StatisticCountTitleResponse

    @Headers("Content-Type: application/json", "Accept: application/json")
    @GET("gudang/{id}/statistic/delivery-order")
    suspend fun getStatistikSuratJalanByGudang(
        @Header("Authorization") token: String,
        @Path("id") id: String,
        @Query("tipe") tipe: String,
    ): StatisticCountTitleResponse

    @Headers("Content-Type: application/json", "Accept: application/json")
    @GET("gudang/{id}/active/surat-jalan")
    suspend fun getActiveSuratJalanByGudang(
        @Header("Authorization") token: String,
        @Path("id") id: String,
        @Query("tipe") tipe: String,
    ): AllSuratJalanResponse

    @Headers("Content-Type: application/json", "Accept: application/json")
    @GET("gudang/{id}/active/delivery-order")
    suspend fun getActiveDeliveryOrderByGudang(
        @Header("Authorization") token: String,
        @Path("id") id: String,
    ): AllDeliveryOrderResponse

    @Headers("Content-Type: application/json", "Accept: application/json")
    @DELETE("gudang/{id}")
    suspend fun deleteGudang(
        @Header("Authorization") token: String,
        @Path("id") id: String,
    ): ErrorMessageResponse

    @Multipart
    @POST("gudang")
    suspend fun addGudang(
        @Header("Authorization") token: String,
        @Part("nama") nama: RequestBody,
        @Part("alamat") alamat: RequestBody,
        @Part("provinsi") provinsi: RequestBody,
        @Part("kota") kota: RequestBody,
        @Part("latitude") latitude: RequestBody,
        @Part("longitude") longitude: RequestBody,
        @Part gambar: MultipartBody.Part,
    ): ErrorMessageWithIdResponse

    @Multipart
    @POST("gudang/update")
    suspend fun updateGudang(
        @Header("Authorization") token: String,
        @Part("id") id: RequestBody,
        @Part("nama") nama: RequestBody,
        @Part("alamat") alamat: RequestBody,
        @Part("provinsi") provinsi: RequestBody,
        @Part("kota") kota: RequestBody,
        @Part("latitude") latitude: RequestBody,
        @Part("longitude") longitude: RequestBody,
        @Part gambar: MultipartBody.Part? = null,
    ): ErrorMessageWithIdResponse
}