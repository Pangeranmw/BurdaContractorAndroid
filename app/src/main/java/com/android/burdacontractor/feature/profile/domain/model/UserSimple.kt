package com.android.burdacontractor.feature.profile.domain.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserSimple(

    @field:SerializedName("id")
    val id: String,

    @field:SerializedName("nama")
    val nama: String,

    @field:SerializedName("foto")
    val foto: String? = null,

    @field:SerializedName("no_hp")
    val noHp: String,

    @field:SerializedName("role")
    val role: String
) : Parcelable