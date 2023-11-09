package com.android.burdacontractor.feature.suratjalan.domain.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class MenanganiUser(

    @field:SerializedName("role")
    val role: String,

    @field:SerializedName("nama")
    val nama: String,

    @field:SerializedName("no_hp")
    val noHp: String,

    @field:SerializedName("foto")
    val foto: String? = null,

    @field:SerializedName("id")
    val id: String
) : Parcelable