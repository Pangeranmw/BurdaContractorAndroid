package com.android.burdacontractor.core.domain.model

class LogisticCoordinate(
    val latitude: Double,
    val longitude: Double,
    val bearing: Double,
    val speed: Double,
    val accuracy: Double
    ) {
    constructor(): this(
        0.0, 0.0,
        0.0, 0.0,0.0
    )
}