package com.android.burdacontractor.feature.suratjalan.data.source.remote.response

import android.os.Parcelable
import com.android.burdacontractor.feature.suratjalan.domain.model.PeminjamanSuratJalan
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class GetAvailablePeminjamanByProyekResponse(
    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("peminjaman")
    val peminjaman: List<PeminjamanSuratJalan>
) : Parcelable