package com.android.burdacontractor.feature.proyek.domain.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class ActiveSjDoLocation(

    @field:SerializedName("nama")
    val nama: String,

    @field:SerializedName("latitude")
    val latitude: String,

    @field:SerializedName("kode")
    val kode: String,

    @field:SerializedName("tipe")
    val tipe: String,

    @field:SerializedName("longitude")
    val longitude: String,

    @field:SerializedName("alamat")
    val alamat: String
) : Parcelable