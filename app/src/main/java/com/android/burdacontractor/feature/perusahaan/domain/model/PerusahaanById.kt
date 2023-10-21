package com.android.burdacontractor.feature.perusahaan.domain.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class PerusahaanById(

    @field:SerializedName("provinsi")
    val provinsi: String,

    @field:SerializedName("kota")
    val kota: String,

    @field:SerializedName("nama")
    val nama: String,

    @field:SerializedName("updated_at")
    val updatedAt: Long,

    @field:SerializedName("latitude")
    val latitude: Double,

    @field:SerializedName("created_at")
    val createdAt: Long,

    @field:SerializedName("id")
    val id: String,

    @field:SerializedName("gambar")
    val gambar: String,

    @field:SerializedName("alamat")
    val alamat: String,

    @field:SerializedName("longitude")
    val longitude: Double
) : Parcelable