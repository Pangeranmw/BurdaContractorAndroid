package com.android.burdacontractor.feature.perusahaan.data.source.remote.response

import android.os.Parcelable
import com.android.burdacontractor.feature.perusahaan.domain.model.AllPerusahaan
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class GetAllPerusahaanResponse(

    @field:SerializedName("perusahaan")
    val perusahaan: List<AllPerusahaan>,

    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String
) : Parcelable