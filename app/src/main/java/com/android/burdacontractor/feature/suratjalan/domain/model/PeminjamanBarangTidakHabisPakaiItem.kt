package com.android.burdacontractor.feature.suratjalan.domain.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class PeminjamanBarangTidakHabisPakaiItem(

    @field:SerializedName("merk")
    val merk: String,

    @field:SerializedName("nama")
    val nama: String,

    @field:SerializedName("nomor_seri")
    val nomorSeri: String,

    @field:SerializedName("id")
    val id: String,

    @field:SerializedName("gambar")
    val gambar: String
) : Parcelable