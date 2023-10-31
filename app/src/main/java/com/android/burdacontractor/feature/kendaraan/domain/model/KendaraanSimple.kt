package com.android.burdacontractor.feature.kendaraan.domain.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class KendaraanSimple(

    @field:SerializedName("merk")
    val merk: String,

    @field:SerializedName("jenis")
    val jenis: String,

    @field:SerializedName("plat_nomor")
    val platNomor: String,

    @field:SerializedName("gambar")
    val gambar: String,

    @field:SerializedName("id")
    val id: String
) : Parcelable