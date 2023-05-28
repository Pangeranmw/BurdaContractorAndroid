package com.android.burdacontractor.core.domain.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Supervisor(
    var userId: String? = null,
    var kodeSv: String? = null,
): Parcelable

