package com.android.burdacontractor.core.data.source.remote.network

import com.android.burdacontractor.core.data.source.remote.response.AddSuratJalanResponse
import com.android.burdacontractor.core.data.source.remote.response.AllSuratJalanResponse
import com.android.burdacontractor.core.data.source.remote.response.ErrorMessageResponse
import com.android.burdacontractor.core.data.source.remote.response.SuratJalanDetailResponse
import com.android.burdacontractor.core.data.source.remote.response2.ListTourismResponse
import com.android.burdacontractor.core.domain.model.enum.SuratJalanTipe
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface SuratJalanService {
    @FormUrlEncoded
    @GET("surat-jalan/all")
    suspend fun getAllSuratJalan(
        @Header("Authorization") token: String,
        @Query("tipe") tipe: String,
        @Query("status") status: String,
        @Query("date_start") date_start: String? = null,
        @Query("date_end") date_end: String? = null,
        @Query("page") page: Int = 1,
        @Query("size") size: Int = 5,
        @Query("search") search: String? = null,
    ): AllSuratJalanResponse

    @FormUrlEncoded
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
    ): AddSuratJalanResponse

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
    ): AddSuratJalanResponse

    @FormUrlEncoded
    @POST("surat-jalan")
    suspend fun addSuratJalanPengembalian(
        @Header("Authorization") token: String,
        @Field("admin_gudang_id") adminGudangId: String,
        @Field("logistic_id") logisticId: String,
        @Field("kendaraan_id") kendaraanId: String,
        @Field("pengembalian_id") pengembalianId: String,
        @Field("tipe") tipe: String = SuratJalanTipe.PENGEMBALIAN.name,
    ): AddSuratJalanResponse

    @FormUrlEncoded
    @POST("surat-jalan")
    suspend fun updateSuratJalanPengirimanGp(
        @Header("Authorization") token: String,
        @Field("admin_gudang_id") adminGudangId: String,
        @Field("logistic_id") logisticId: String,
        @Field("kendaraan_id") kendaraanId: String,
        @Field("peminjaman_id") peminjamanId: String,
        @Field("tipe") tipe: String = SuratJalanTipe.PENGIRIMAN_GUDANG_PROYEK.name,
    ): AddSuratJalanResponse

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
    ): AddSuratJalanResponse

    @FormUrlEncoded
    @POST("surat-jalan")
    suspend fun updateSuratJalanPengembalian(
        @Header("Authorization") token: String,
        @Field("admin_gudang_id") adminGudangId: String,
        @Field("logistic_id") logisticId: String,
        @Field("kendaraan_id") kendaraanId: String,
        @Field("pengembalian_id") pengembalianId: String,
        @Field("tipe") tipe: String = SuratJalanTipe.PENGEMBALIAN.name,
    ): AddSuratJalanResponse

    @FormUrlEncoded
    @DELETE("surat-jalan/{surat_jalan_id}")
    suspend fun deleteSuratJalan(
        @Header("Authorization") token: String,
        @Path("surat_jalan_id") suratJalanId: String,
    ): ErrorMessageResponse
}