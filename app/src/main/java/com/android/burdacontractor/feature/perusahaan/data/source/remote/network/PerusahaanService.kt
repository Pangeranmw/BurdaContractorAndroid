package com.android.burdacontractor.feature.perusahaan.data.source.remote.network

import com.android.burdacontractor.core.data.source.remote.response.ErrorMessageResponse
import com.android.burdacontractor.core.data.source.remote.response.ErrorMessageWithIdResponse
import com.android.burdacontractor.feature.deliveryorder.data.source.remote.response.AllDeliveryOrderResponse
import com.android.burdacontractor.feature.perusahaan.data.source.remote.response.GetAllPerusahaanResponse
import com.android.burdacontractor.feature.perusahaan.data.source.remote.response.GetPerusahaanByIdResponse
import com.android.burdacontractor.feature.perusahaan.data.source.remote.response.GetPerusahaanProvinsiResponse
import com.android.burdacontractor.feature.suratjalan.data.source.remote.response.StatisticCountTitleResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface PerusahaanService {
    @Headers("Content-Type: application/json", "Accept: application/json")
    @GET("perusahaan/{id}")
    suspend fun getPerusahaanById(
        @Header("Authorization") token: String,
        @Path("id") id: String,
    ): GetPerusahaanByIdResponse

    @Headers("Content-Type: application/json", "Accept: application/json")
    @GET("perusahaans")
    suspend fun getAllPerusahaan(
        @Header("Authorization") token: String,
        @Query("page") page: Int = 1,
        @Query("size") size: Int = 5,
        @Query("search") search: String? = null,
        @Query("filter") provinsi: String? = null,
        @Query("coordinate") coordinate: String? = null,
    ): GetAllPerusahaanResponse

    @Headers("Content-Type: application/json", "Accept: application/json")
    @GET("perusahaan/filter/provinsi")
    suspend fun getPerusahaanProvinsi(
        @Header("Authorization") token: String,
    ): GetPerusahaanProvinsiResponse

    @Headers("Content-Type: application/json", "Accept: application/json")
    @GET("perusahaan/{id}/statistic/delivery-order")
    suspend fun getStatistikDeliveryOrderByPerusahaan(
        @Header("Authorization") token: String,
        @Path("id") id: String,
    ): StatisticCountTitleResponse

    @Headers("Content-Type: application/json", "Accept: application/json")
    @GET("perusahaan/{id}/active/delivery-order")
    suspend fun getActiveDeliveryOrderByPerusahaan(
        @Header("Authorization") token: String,
        @Path("id") id: String,
    ): AllDeliveryOrderResponse

    @Headers("Content-Type: application/json", "Accept: application/json")
    @DELETE("perusahaan/{id}")
    suspend fun deletePerusahaan(
        @Header("Authorization") token: String,
        @Path("id") id: String,
    ): ErrorMessageResponse

    @Multipart
    @POST("perusahaan")
    suspend fun addPerusahaan(
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
    @POST("perusahaan/update")
    suspend fun updatePerusahaan(
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
