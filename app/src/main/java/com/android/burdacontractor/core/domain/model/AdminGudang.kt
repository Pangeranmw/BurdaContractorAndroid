package com.android.burdacontractor.core.domain.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class AdminGudang(
    var userId: String? = null,
    var kodeAg: String? = null,
    var gudangId: String? = null,
    var gudang: Gudang? = null,
    var user: User? = null,
): Parcelable