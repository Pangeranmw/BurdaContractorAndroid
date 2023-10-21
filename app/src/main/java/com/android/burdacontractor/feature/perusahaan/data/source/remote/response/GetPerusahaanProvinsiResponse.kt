package com.android.burdacontractor.feature.perusahaan.data.source.remote.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class GetPerusahaanProvinsiResponse(

    @field:SerializedName("provinsi")
    val provinsi: List<String>,

    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String
) : Parcelable