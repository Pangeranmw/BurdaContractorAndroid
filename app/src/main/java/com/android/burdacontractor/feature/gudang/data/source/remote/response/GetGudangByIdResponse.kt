package com.android.burdacontractor.feature.gudang.data.source.remote.response

import com.android.burdacontractor.feature.gudang.domain.model.GudangById
import com.google.gson.annotations.SerializedName


data class GetGudangByIdResponse(

    @field:SerializedName("gudang")
    val gudang: GudangById,

    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String
)

