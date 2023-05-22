package com.android.burdacontractor.core.domain.model

class LogisticCoordinate(
    val latitude: Double,
    val longitude: Double) {
    // Null default values create a no-argument default constructor, which is needed
    // for deserialization from a DataSnapshot.
}