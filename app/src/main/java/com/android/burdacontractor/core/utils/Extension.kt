package com.android.burdacontractor.core.utils

import android.view.View
import com.android.burdacontractor.core.data.source.remote.response.DistanceMatrixResponse
import com.android.burdacontractor.core.data.source.remote.response.Routes
import com.android.burdacontractor.core.domain.model.LogisticCoordinate
import com.google.android.material.snackbar.Snackbar

// Extension For Distance Matrix Response
fun List<Routes>.getDuration(): String{
    val duration = this[0].duration
    return when(duration.toInt()){
        in 0..60 -> String.format("%.2f Dtk", duration)
        in 60..3600 -> String.format("%.2f Mnt", duration/60)
        else -> String.format("%.2f Jam", duration/3600)
    }
}
fun List<Routes>.getDistance(): String {
    val distance = this[0].distance
    return when (distance.toInt()) {
        in 0..1000 -> String.format("%.2f Mtr", distance)
        else -> String.format("%.2f Km", distance / 1000)
    }
}

fun LogisticCoordinate.combine(): String{
    return "${this.latitude}|${this.longitude}"
}

fun getCoordinate(originCoordinate: String, destinationCoordinate: String): String{
    val originLat = originCoordinate.split("|")[0]
    val originLon = originCoordinate.split("|")[1]
    val destinationLat = destinationCoordinate.split("|")[0]
    val destinationLon = destinationCoordinate.split("|")[1]
    return "$originLon,$originLat;$destinationLon,$destinationLat"
}

fun View.setGone() {
    this.visibility = View.GONE
}

fun View.setVisible() {
    this.visibility = View.VISIBLE
}