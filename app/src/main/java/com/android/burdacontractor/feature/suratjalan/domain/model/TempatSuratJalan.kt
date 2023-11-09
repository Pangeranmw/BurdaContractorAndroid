package com.android.burdacontractor.feature.suratjalan.domain.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class TempatSuratJalan(

    @field:SerializedName("id")
    val id: String,

    @field:SerializedName("coordinate")
    val coordinate: String,

    @field:SerializedName("nama")
    val nama: String,

    @field:SerializedName("foto")
    val foto: String? = null,

    @field:SerializedName("alamat")
    val alamat: String
) : Parcelable