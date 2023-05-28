package com.android.burdacontractor.core.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class AddUpdateResponse (
    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("id")
    val id: String,
)