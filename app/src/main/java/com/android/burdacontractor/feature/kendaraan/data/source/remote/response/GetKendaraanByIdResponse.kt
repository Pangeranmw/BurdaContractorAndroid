package com.android.burdacontractor.feature.kendaraan.data.source.remote.response

import android.os.Parcelable
import com.android.burdacontractor.feature.kendaraan.domain.model.Kendaraan
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class GetKendaraanByIdResponse(

    @field:SerializedName("kendaraan")
    val kendaraan: Kendaraan,

    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String
) : Parcelable