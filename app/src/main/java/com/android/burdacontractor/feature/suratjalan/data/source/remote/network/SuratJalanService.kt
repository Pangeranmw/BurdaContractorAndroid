package com.android.burdacontractor.feature.suratjalan.data.source.remote.network

import com.android.burdacontractor.core.data.source.remote.response.CountActiveResponse
import com.android.burdacontractor.core.data.source.remote.response.ErrorMessageResponse
import com.android.burdacontractor.core.data.source.remote.response.ErrorMessageWithIdResponse
import com.android.burdacontractor.feature.suratjalan.data.source.remote.request.AddUpdateSuratJalanBody
import com.android.burdacontractor.feature.suratjalan.data.source.remote.response.AllSuratJalanResponse
import com.android.burdacontractor.feature.suratjalan.data.source.remote.response.AllSuratJalanWithCountResponse
import com.android.burdacontractor.feature.suratjalan.data.source.remote.response.GetAvailablePeminjamanByProyekResponse
import com.android.burdacontractor.feature.suratjalan.data.source.remote.response.GetAvailablePenggunaanByProyekResponse
import com.android.burdacontractor.feature.suratjalan.data.source.remote.response.GetAvailableProyekBySuratJalanTypeResponse
import com.android.burdacontractor.feature.suratjalan.data.source.remote.response.StatisticCountTitleResponse
import com.android.burdacontractor.feature.suratjalan.data.source.remote.response.SuratJalanDetailResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface SuratJalanService {
    @Headers("Content-Type: application/json", "Accept: application/json")
    @GET("surat-jalans")
    suspend fun getAllSuratJalan(
        @Header("Authorization") token: String,
        @Query("tipe") tipe: String,
        @Query("status") status: String,
        @Query("date_start") date_start: String? = null,
        @Query("date_end") date_end: String? = null,
        @Query("page") page: Int = 1,
        @Query("size") size: Int = 5,
        @Query("search") search: String? = null,
        @Query("for") createdByOrFor: String,
    ): AllSuratJalanResponse

    @Headers("Content-Type: application/json","Accept: application/json")
    @GET("surat-jalan/menunggu-surat-jalan/count")
    suspend fun getStatistikMenungguSuratJalan(
        @Header("Authorization") token: String,
    ): StatisticCountTitleResponse

    @Headers("Content-Type: application/json", "Accept: application/json")
    @GET("surat-jalans/dalam-perjalanan")
    suspend fun getAllSuratJalanDalamPerjalananByUser(
        @Header("Authorization") token: String,
    ): AllSuratJalanResponse

    @Headers("Content-Type: application/json","Accept: application/json")
    @GET("surat-jalan/active")
    suspend fun getSomeActiveSuratJalan(
        @Header("Authorization") token: String,
        @Query("tipe") tipe: String,
        @Query("size") size: Int = 5,
    ): AllSuratJalanWithCountResponse

    @Headers("Content-Type: application/json","Accept: application/json")
    @GET("surat-jalan/active/count")
    suspend fun getCountActiveSuratJalan(
        @Header("Authorization") token: String,
    ): CountActiveResponse

    @Headers("Content-Type: application/json", "Accept: application/json")
    @GET("surat-jalan/{id}")
    suspend fun getSuratJalanById(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): SuratJalanDetailResponse

    @Headers("Content-Type: application/json", "Accept: application/json")
    @GET("surat-jalan/proyek/available")
    suspend fun getAvailableProyekBySuratJalanType(
        @Header("Authorization") token: String,
        @Query("tipe") tipe: String
    ): GetAvailableProyekBySuratJalanTypeResponse

    @Headers("Content-Type: application/json", "Accept: application/json")
    @GET("surat-jalan/peminjaman/available")
    suspend fun getAvailablePeminjamanByProyek(
        @Header("Authorization") token: String,
        @Query("tipe") tipe: String,
        @Query("proyek_id") proyekId: String,
    ): GetAvailablePeminjamanByProyekResponse

    @Headers("Content-Type: application/json", "Accept: application/json")
    @GET("surat-jalan/penggunaan/available")
    suspend fun getAvailablePenggunaanByProyek(
        @Header("Authorization") token: String,
        @Query("tipe") tipe: String,
        @Query("proyek_id") proyekId: String,
    ): GetAvailablePenggunaanByProyekResponse

    @Headers("Content-Type: application/json", "Accept: application/json")
    @PUT("surat-jalan/{id}")
    suspend fun updateSuratJalan(
        @Header("Authorization") token: String,
        @Path("id") id: String,
        @Body addUpdateSuratJalan: AddUpdateSuratJalanBody
    ): ErrorMessageWithIdResponse

    @Headers("Content-Type: application/json", "Accept: application/json")
    @POST("surat-jalan")
    suspend fun addSuratJalan(
        @Header("Authorization") token: String,
        @Body addUpdateSuratJalan: AddUpdateSuratJalanBody
    ): ErrorMessageWithIdResponse

    @Headers("Content-Type: application/json", "Accept: application/json")
    @DELETE("surat-jalan/{id}")
    suspend fun deleteSuratJalan(
        @Header("Authorization") token: String,
        @Path("id") suratJalanId: String,
    ): ErrorMessageResponse

    @Headers("Content-Type: application/json", "Accept: application/json")
    @DELETE("surat-jalan/child/{id}")
    suspend fun deleteSuratJalanChild(
        @Header("Authorization") token: String,
        @Path("id") suratJalanId: String,
        @Query("tipe") tipe: String,
    ): ErrorMessageResponse

    @Headers("Content-Type: application/json", "Accept: application/json")
    @POST("surat-jalan/{id}/mark-deliver")
    suspend fun sendSuratJalan(
        @Header("Authorization") token: String,
        @Path("id") suratJalanId: String,
    ): ErrorMessageResponse

    @Headers("Content-Type: application/json", "Accept: application/json")
    @POST("surat-jalan/{id}/give-ttd")
    suspend fun giveTtdSuratJalan(
        @Header("Authorization") token: String,
        @Path("id") suratJalanId: String,
    ): ErrorMessageResponse

    @Headers("Content-Type: application/json", "Accept: application/json")
    @POST("surat-jalan/{id}/mark-complete")
    suspend fun markCompleteSuratJalan(
        @Header("Authorization") token: String,
        @Path("id") suratJalanId: String,
    ): ErrorMessageResponse

    @Multipart
    @POST("surat-jalan/upload-photo")
    suspend fun uploadFotoBuktiSuratJalan(
        @Header("Authorization") token: String,
        @Part("id") id: RequestBody,
        @Part foto: MultipartBody.Part,
    ): ErrorMessageResponse
}