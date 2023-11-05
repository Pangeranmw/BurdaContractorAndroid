package com.android.burdacontractor.core.data.source.remote.network

import com.android.burdacontractor.core.data.source.remote.response.GetCityByProvinceResponse
import com.android.burdacontractor.core.data.source.remote.response.GetProvinceResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface DaerahService {
    @GET("city/{province}/array")
    suspend fun getCityByProvince(
        @Path("province") province: String,
    ): GetCityByProvinceResponse

    @GET("province/array")
    suspend fun getProvince(): GetProvinceResponse
}