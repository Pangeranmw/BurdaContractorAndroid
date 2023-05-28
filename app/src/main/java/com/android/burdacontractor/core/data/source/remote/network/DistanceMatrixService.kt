package com.android.burdacontractor.core.data.source.remote.network

import com.android.burdacontractor.core.data.source.remote.response.DistanceMatrixResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface DistanceMatrixService {
    @GET("driving/{coordinates}")
    suspend fun getDistanceMatrixOSRM(
        @Path("coordinates") coordinates: String,
        @Query("skip_waypoints") skip_waypoints: Boolean = true,
    ): DistanceMatrixResponse
}