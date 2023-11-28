package com.android.burdacontractor.feature.suratjalan.data.source.remote.response

import android.os.Parcelable
import com.android.burdacontractor.feature.suratjalan.domain.model.PenggunaanSuratJalan
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class GetAvailablePenggunaanByProyekResponse(
    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("penggunaan")
    val penggunaan: List<PenggunaanSuratJalan>
) : Parcelable