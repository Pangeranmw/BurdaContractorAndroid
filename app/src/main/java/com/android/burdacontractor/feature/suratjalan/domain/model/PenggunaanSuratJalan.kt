package com.android.burdacontractor.feature.suratjalan.domain.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class PenggunaanSuratJalan(
    @field:SerializedName("sj_child_id")
    val sjChildId: String? = null,

    @field:SerializedName("id")
    val id: String,

    @field:SerializedName("created_at")
    val createdAt: Long,

    @field:SerializedName("menangani_user")
    val menanganiUser: MenanganiUser,

    @field:SerializedName("menangani_asal_user")
    val menanganiAsalUser: MenanganiUser? = null,

    @field:SerializedName("asal")
    val asal: TempatSuratJalan,

    @field:SerializedName("barang")
    val barang: List<PenggunaanBarangHabisPakaiItem>,

    @field:SerializedName("kode")
    val kode: String
) : Parcelable
