package com.android.burdacontractor.core.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class DistanceMatrixResponse(
    val code: String,
    val routes: List<Routes>,
    val message: String
)
data class Routes(
    val geometry: String,
    val legs: List<Legs>,
    @SerializedName("weight_name")
    val weightName: String,
    val weight: Double,
    val duration: Double,
    val distance: Double
)
data class Legs(
    val steps: List<RouteStep>,
    val summary: String,
    val weight: Double,
    val duration: Double,
    val distance: Double
)
data class RouteStep(
    val distance: Double,
    val duration: Double,
    val geometry: String,
    val weight: Double,
)