package com.android.burdacontractor.core.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.view.View
import androidx.fragment.app.Fragment
import com.android.burdacontractor.core.data.source.remote.response.Routes
import com.android.burdacontractor.core.domain.model.LogisticCoordinate

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

// Lambda with receiver (extras: Bundle.() -> Unit = {})
fun <T> Context.openActivityWithExtras(it: Class<T>, extras: Bundle.() -> Unit = {}, activity: Activity) {
    val intent = Intent(this, it)
    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
    intent.putExtras(Bundle().apply(extras))
    startActivity(intent)
    activity.overridePendingTransition(0, 0)
}
fun <T> Context.openActivity(it: Class<T>, activity: Activity) {
    val intent = Intent(this, it)
    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
    startActivity(intent)
    activity.overridePendingTransition(0, 0)
}
fun setParcelable(fragment: Fragment, parcelable: Bundle.() -> Unit = {}) {
    val bundle = Bundle()
    bundle.apply(parcelable)
    fragment.arguments = bundle
}