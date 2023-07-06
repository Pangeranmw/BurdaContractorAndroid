package com.android.burdacontractor.feature.kendaraan.data.source.remote.network

import com.android.burdacontractor.core.data.source.remote.response.ErrorMessageResponse
import com.android.burdacontractor.feature.kendaraan.data.source.remote.response.GetKendaraanByLogisticResponse
import okhttp3.MultipartBody
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface KendaraanService {
    @GET("kendaraan/logistic")
    suspend fun getKendaraanByLogistic(
        @Header("Authorization") token: String,
    ): GetKendaraanByLogisticResponse

    @Multipart
    @POST("kendaraan")
    suspend fun addKendaraan(
        @Header("Authorization") token: String,
        @Part("foto") foto: MultipartBody.Part,
    ): ErrorMessageResponse
}