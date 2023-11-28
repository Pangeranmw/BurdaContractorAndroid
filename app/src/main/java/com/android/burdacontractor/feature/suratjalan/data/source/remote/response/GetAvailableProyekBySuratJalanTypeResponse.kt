package com.android.burdacontractor.feature.suratjalan.data.source.remote.response

import android.os.Parcelable
import com.android.burdacontractor.core.domain.model.Proyek
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class GetAvailableProyekBySuratJalanTypeResponse(
    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("proyek")
    val proyek: List<Proyek>
) : Parcelable