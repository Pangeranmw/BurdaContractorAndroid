package com.android.burdacontractor.feature.suratjalan.domain.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class PenggunaanBarangHabisPakaiItem(

    @field:SerializedName("ukuran")
    val ukuran: String,

    @field:SerializedName("merk")
    val merk: String? = null,

    @field:SerializedName("nama")
    val nama: String,

    @field:SerializedName("jumlah_satuan")
    val jumlahSatuan: String,

    @field:SerializedName("id")
    val id: String,

    @field:SerializedName("gambar")
    val gambar: String? = null

) : Parcelable