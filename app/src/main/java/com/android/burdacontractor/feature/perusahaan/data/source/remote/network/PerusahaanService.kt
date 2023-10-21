package com.android.burdacontractor.feature.perusahaan.data.source.remote.network

import com.android.burdacontractor.feature.perusahaan.data.source.remote.response.GetAllPerusahaanResponse
import com.android.burdacontractor.feature.perusahaan.data.source.remote.response.GetPerusahaanByIdResponse
import com.android.burdacontractor.feature.perusahaan.data.source.remote.response.GetPerusahaanProvinsiResponse
import retrofit2.http.*

interface PerusahaanService {
    @Headers("Content-Type: application/json", "Accept: application/json")
    @GET("perusahaan/{id}")
    suspend fun getPerusahaanById(
        @Header("Authorization") token: String,
        @Path("id") id: String,
    ): GetPerusahaanByIdResponse

    @Headers("Content-Type: application/json", "Accept: application/json")
    @GET("perusahaan/all")
    suspend fun getAllPerusahaan(
        @Header("Authorization") token: String,
        @Query("page") page: Int = 1,
        @Query("size") size: Int = 5,
        @Query("search") search: String? = null,
        @Query("filter") provinsi: String? = null,
        @Query("coordinate") coordinate: String? = null,
    ): GetAllPerusahaanResponse

    @Headers("Content-Type: application/json", "Accept: application/json")
    @GET("perusahaan/provinsi")
    suspend fun getPerusahaanProvinsi(
        @Header("Authorization") token: String,
    ): GetPerusahaanProvinsiResponse

}
