package com.android.burdacontractor.core.data.source.remote.response2

import com.android.burdacontractor.core.domain.model.User
import com.google.gson.annotations.SerializedName

data class LoginResponse(

    @field:SerializedName("code")
    val code: Boolean,

    @field:SerializedName("error")
    val error: String,

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("places")
    val data: DataLoginResponse
)

data class DataLoginResponse(
    @SerializedName("user")
    val user: User
)