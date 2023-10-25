package com.android.burdacontractor.feature.kendaraan.data.source.remote.response

import android.os.Parcelable
import com.android.burdacontractor.feature.kendaraan.domain.model.AllKendaraan
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class GetAllKendaraanResponse(

    @field:SerializedName("kendaraan")
    val kendaraan: List<AllKendaraan>,

    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String
) : Parcelable