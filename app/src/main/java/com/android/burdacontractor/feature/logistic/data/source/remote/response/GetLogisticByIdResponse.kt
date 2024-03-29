package com.android.burdacontractor.feature.logistic.data.source.remote.response

import com.android.burdacontractor.feature.logistic.domain.model.LogisticById
import com.google.gson.annotations.SerializedName

data class GetLogisticByIdResponse(

    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("logistic")
    val logistic: LogisticById
)