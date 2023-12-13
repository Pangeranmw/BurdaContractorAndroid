package com.android.burdacontractor.feature.profile.data.source.remote.response

import com.android.burdacontractor.feature.profile.domain.model.User
import com.google.gson.annotations.SerializedName

data class GetAllUsersResponse(

    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("users")
    val user: List<User>
)