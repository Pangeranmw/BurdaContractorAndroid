package com.android.burdacontractor.feature.kendaraan.domain.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Kendaraan(

    @field:SerializedName("merk")
    val merk: String,

    @field:SerializedName("gudang_id")
    val gudangId: String,

    @field:SerializedName("updated_at")
    val updatedAt: Long,

    @field:SerializedName("logistic_id")
    val logisticId: String,

    @field:SerializedName("jenis")
    val jenis: String,

    @field:SerializedName("created_at")
    val createdAt: Long,

    @field:SerializedName("id")
    val id: String,

    @field:SerializedName("plat_nomor")
    val platNomor: String,

    @field:SerializedName("gambar")
    val gambar: String,

    @field:SerializedName("status")
    val status: String
) : Parcelable
