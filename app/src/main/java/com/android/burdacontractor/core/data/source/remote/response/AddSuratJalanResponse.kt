package com.android.burdacontractor.core.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class AddSuratJalanResponse (
    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("surat_jalan_id")
    val suratJalanId: String,
)