package com.android.burdacontractor.feature.suratjalan.domain.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class TtdVerification(

    @field:SerializedName("role")
    val role: String,

    @field:SerializedName("sebagai")
    val sebagai: String,

    @field:SerializedName("nama")
    val nama: String,

    @field:SerializedName("ttd")
    val ttd: String
) : Parcelable