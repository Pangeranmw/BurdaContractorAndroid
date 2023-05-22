package com.android.burdacontractor.core.data.source.remote.network

import com.android.burdacontractor.core.data.source.remote.response.AllSuratJalanResponse
import com.android.burdacontractor.core.data.source.remote.response2.ListTourismResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface ApiService {
    @GET("list")
    suspend fun getList(): ListTourismResponse

}