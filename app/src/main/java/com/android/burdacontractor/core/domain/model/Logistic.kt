package com.android.burdacontractor.core.domain.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Logistic(
    var userId: String? = null,
    var kodeLogistic: String? = null,
    var latitude: Double? = null,
    var longitude: Double? = null,
    var user: User? = null,
): Parcelable
