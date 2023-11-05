package com.android.burdacontractor.core.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class GetCityByProvinceResponse(
    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("kota")
    val kota: List<String>,
)