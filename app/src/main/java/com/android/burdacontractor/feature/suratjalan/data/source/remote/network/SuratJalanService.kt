package com.android.burdacontractor.feature.suratjalan.data.source.remote.network

import com.android.burdacontractor.core.data.source.remote.response.CountActiveResponse
import com.android.burdacontractor.core.data.source.remote.response.ErrorMessageResponse
import com.android.burdacontractor.core.domain.model.enums.SuratJalanTipe
import com.android.burdacontractor.feature.suratjalan.data.source.remote.response.AllSuratJalanResponse
import com.android.burdacontractor.feature.suratjalan.data.source.remote.response.AllSuratJalanWithCountResponse
import com.android.burdacontractor.feature.suratjalan.data.source.remote.response.StatisticCountTitleResponse
import com.android.burdacontractor.feature.suratjalan.data.source.remote.response.SuratJalanDetailResponse
import okhttp3.MultipartBody
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

    @Headers("Content-Type: application/json","Accept: application/json")
    @GET("surat-jalan/{id}")
    suspend fun getSuratJalanById(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): SuratJalanDetailResponse

    @FormUrlEncoded
    @POST("surat-jalan")
    suspend fun addSuratJalanPengirimanGp(
        @Header("Authorization") token: String,
        @Field("admin_gudang_id") adminGudangId: String,
        @Field("logistic_id") logisticId: String,
        @Field("kendaraan_id") kendaraanId: String,
        @Field("peminjaman_id") peminjamanId: String,
        @Field("tipe") tipe: String = SuratJalanTipe.PENGIRIMAN_GUDANG_PROYEK.name,
    ): ErrorMessageResponse

    @FormUrlEncoded
    @POST("surat-jalan")
    suspend fun addSuratJalanPengirimanPp(
        @Header("Authorization") token: String,
        @Field("admin_gudang_id") adminGudangId: String,
        @Field("logistic_id") logisticId: String,
        @Field("kendaraan_id") kendaraanId: String,
        @Field("peminjaman_asal_id") peminjamanAsalId: String,
        @Field("peminjaman_tujuan_id") peminjamanTujuanId: String,
        @Field("tipe") tipe: String = SuratJalanTipe.PENGIRIMAN_PROYEK_PROYEK.name,
    ): ErrorMessageResponse

    @FormUrlEncoded
    @POST("surat-jalan")
    suspend fun addSuratJalanPengembalian(
        @Header("Authorization") token: String,
        @Field("admin_gudang_id") adminGudangId: String,
        @Field("logistic_id") logisticId: String,
        @Field("kendaraan_id") kendaraanId: String,
        @Field("pengembalian_id") pengembalianId: String,
        @Field("tipe") tipe: String = SuratJalanTipe.PENGEMBALIAN.name,
    ): ErrorMessageResponse

    @FormUrlEncoded
    @POST("surat-jalan")
    suspend fun updateSuratJalanPengirimanGp(
        @Header("Authorization") token: String,
        @Field("admin_gudang_id") adminGudangId: String,
        @Field("logistic_id") logisticId: String,
        @Field("kendaraan_id") kendaraanId: String,
        @Field("peminjaman_id") peminjamanId: String,
        @Field("tipe") tipe: String = SuratJalanTipe.PENGIRIMAN_GUDANG_PROYEK.name,
    ): ErrorMessageResponse

    @FormUrlEncoded
    @POST("surat-jalan")
    suspend fun updateSuratJalanPengirimanPp(
        @Header("Authorization") token: String,
        @Field("admin_gudang_id") adminGudangId: String,
        @Field("logistic_id") logisticId: String,
        @Field("kendaraan_id") kendaraanId: String,
        @Field("peminjaman_asal_id") peminjamanAsalId: String,
        @Field("peminjaman_tujuan_id") peminjamanTujuanId: String,
        @Field("tipe") tipe: String = SuratJalanTipe.PENGIRIMAN_PROYEK_PROYEK.name,
    ): ErrorMessageResponse

    @FormUrlEncoded
    @POST("surat-jalan")
    suspend fun updateSuratJalanPengembalian(
        @Header("Authorization") token: String,
        @Field("admin_gudang_id") adminGudangId: String,
        @Field("logistic_id") logisticId: String,
        @Field("kendaraan_id") kendaraanId: String,
        @Field("pengembalian_id") pengembalianId: String,
        @Field("tipe") tipe: String = SuratJalanTipe.PENGEMBALIAN.name,
    ): ErrorMessageResponse

    @FormUrlEncoded
    @DELETE("surat-jalan/{surat_jalan_id}")
    suspend fun deleteSuratJalan(
        @Header("Authorization") token: String,
        @Path("surat_jalan_id") suratJalanId: String,
    ): ErrorMessageResponse

    @FormUrlEncoded
    @POST("surat-jalan/{surat_jalan_id}/send")
    suspend fun sendSuratJalan(
        @Header("Authorization") token: String,
        @Path("surat_jalan_id") suratJalanId: String,
    ): ErrorMessageResponse

    @FormUrlEncoded
    @POST("surat-jalan/{surat_jalan_id}/mark-complete")
    suspend fun markCompleteSuratJalan(
        @Header("Authorization") token: String,
        @Path("surat_jalan_id") suratJalanId: String,
    ): ErrorMessageResponse

    @FormUrlEncoded
    @POST("surat-jalan/{surat_jalan_id}/add-ttd")
    suspend fun addTTDSuratJalan(
        @Header("Authorization") token: String,
        @Path("surat_jalan_id") suratJalanId: String,
    ): ErrorMessageResponse

    @Multipart
    @POST("surat-jalan/{surat_jalan_id}/upload-foto")
    suspend fun uploadFotoBuktiSuratJalan(
        @Header("Authorization") token: String,
        @Part("foto") foto: MultipartBody.Part,
    ): ErrorMessageResponse
}