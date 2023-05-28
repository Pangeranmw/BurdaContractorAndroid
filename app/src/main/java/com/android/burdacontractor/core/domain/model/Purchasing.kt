package com.android.burdacontractor.core.domain.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Purchasing(
    var userId: String? = null,
    var kodePurchasing: String? = null,
    var user: User? = null,
): Parcelable

