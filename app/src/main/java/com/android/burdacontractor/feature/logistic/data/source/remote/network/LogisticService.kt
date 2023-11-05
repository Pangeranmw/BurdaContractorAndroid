package com.android.burdacontractor.feature.logistic.data.source.remote.network

import com.android.burdacontractor.feature.logistic.data.source.remote.response.GetAllLogisticResponse
import com.android.burdacontractor.feature.logistic.data.source.remote.response.GetLogisticByIdResponse
import retrofit2.http.*

interface LogisticService {
    @Headers("Content-Type: application/json", "Accept: application/json")
    @GET("logistics")
    suspend fun getAllLogistic(
        @Header("Authorization") token: String,
        @Query("page") page: Int = 1,
        @Query("size") size: Int = 5,
        @Query("search") search: String? = null,
        @Query("coordinate") coordinate: String? = null,
    ): GetAllLogisticResponse

    @Headers("Content-Type: application/json", "Accept: application/json")
    @GET("logistic/{id}")
    suspend fun getLogisticById(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): GetLogisticByIdResponse
}