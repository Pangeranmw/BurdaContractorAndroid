package com.android.burdacontractor.feature.perusahaan.domain.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class AllPerusahaan(

    @field:SerializedName("provinsi")
    val provinsi: String,

    @field:SerializedName("kota")
    val kota: String,

    @field:SerializedName("latitude")
    val latitude: Double,

    @field:SerializedName("jarak")
    val jarak: Double? = null,

    @field:SerializedName("durasi")
    val durasi: Double? = null,

    @field:SerializedName("total_data")
    val totalData: Int,

    @field:SerializedName("created_at")
    val createdAt: Long,

    @field:SerializedName("gambar")
    val gambar: String,

    @field:SerializedName("alamat")
    val alamat: String,

    @field:SerializedName("nama")
    val nama: String,

    @field:SerializedName("updated_at")
    val updatedAt: Long,

    @field:SerializedName("do_dalam_perjalanan")
    val doDalamPerjalanan: Int,

    @field:SerializedName("id")
    val id: String,

    @field:SerializedName("longitude")
    val longitude: Double,

    @field:SerializedName("do_menunggu_konfirmasi")
    val doMenungguKonfirmasi: Int
) : Parcelable
