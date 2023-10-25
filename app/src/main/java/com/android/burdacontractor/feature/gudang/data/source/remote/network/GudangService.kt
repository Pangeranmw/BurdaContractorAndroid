package com.android.burdacontractor.feature.gudang.data.source.remote.network

import com.android.burdacontractor.feature.gudang.data.source.remote.response.GetAllGudangResponse
import com.android.burdacontractor.feature.gudang.data.source.remote.response.GetGudangByIdResponse
import com.android.burdacontractor.feature.gudang.data.source.remote.response.GetGudangProvinsiResponse
import retrofit2.http.*

interface GudangService {
    @Headers("Content-Type: application/json", "Accept: application/json")
    @GET("gudang/{id}")
    suspend fun getGudangById(
        @Header("Authorization") token: String,
        @Path("id") id: String,
    ): GetGudangByIdResponse

    @Headers("Content-Type: application/json", "Accept: application/json")
    @GET("gudang/all")
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
}