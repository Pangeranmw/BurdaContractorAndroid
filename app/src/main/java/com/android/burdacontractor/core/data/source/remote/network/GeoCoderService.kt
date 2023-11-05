package com.android.burdacontractor.core.data.source.remote.network

import com.android.burdacontractor.core.data.source.remote.response.GetLocationFromAddressResponse
import com.android.burdacontractor.core.data.source.remote.response.GetLocationFromCoordinateResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface GeoCoderService {
    @GET("reverse")
    suspend fun getLocationFromCoordinate(
        @Query("lat") latitude: String,
        @Query("lon") longitude: String,
    ): GetLocationFromCoordinateResponse

    @GET("search")
    suspend fun getLocationFromAddress(@Query("q") query: String): GetLocationFromAddressResponse
}