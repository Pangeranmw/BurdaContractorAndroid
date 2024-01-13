package com.android.burdacontractor.core.data.source.remote.network

import com.android.burdacontractor.BuildConfig
import com.android.burdacontractor.core.data.source.remote.response.GetPlaceByQueryResponse
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

interface PlaceService {
    @Headers(
        "Content-Type: application/json",
        "X-Goog-Api-Key: ${BuildConfig.PLACE_API_KEY}",
        "X-Goog-FieldMask: places.displayName,places.formattedAddress,places.location"
    )
    @POST("v1/places:searchText")
    suspend fun getPlaceByQuery(
        @Query("textQuery") textQuery: String,
    ): GetPlaceByQueryResponse
}