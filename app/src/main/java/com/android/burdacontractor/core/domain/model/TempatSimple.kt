package com.android.burdacontractor.core.domain.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class TempatSimple(
    @field:SerializedName("coordinate")
    val coordinate: String,

    @field:SerializedName("nama")
    val nama: String,

    @field:SerializedName("gambar")
    val gambar: String,

    @field:SerializedName("alamat")
    val alamat: String,

    @field:SerializedName("id")
    val id: String
) : Parcelable