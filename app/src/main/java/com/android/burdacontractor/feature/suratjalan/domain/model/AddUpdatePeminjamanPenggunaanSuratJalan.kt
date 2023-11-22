package com.android.burdacontractor.feature.suratjalan.domain.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class AddUpdatePeminjamanPenggunaanSuratJalan(
    @field:SerializedName("sj_child_id")
    val sjChildId: String? = null,

    @field:SerializedName("id")
    val id: String,

    @field:SerializedName("menangani_id")
    val menanganiId: String,

    @field:SerializedName("menangani_asal_id")
    val menanganiAsalId: String? = null,

    @field:SerializedName("user_id")
    val userId: String? = null,
) : Parcelable
