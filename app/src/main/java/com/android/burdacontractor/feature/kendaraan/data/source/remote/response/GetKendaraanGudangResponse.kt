package com.android.burdacontractor.feature.kendaraan.data.source.remote.response

import android.os.Parcelable
import com.android.burdacontractor.feature.gudang.domain.model.GudangById
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class GetKendaraanGudangResponse(

    @field:SerializedName("gudang")
    val gudang: List<GudangById>,

    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String
) : Parcelable