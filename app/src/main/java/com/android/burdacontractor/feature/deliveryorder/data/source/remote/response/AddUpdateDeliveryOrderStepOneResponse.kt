package com.android.burdacontractor.feature.deliveryorder.data.source.remote.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class AddUpdateDeliveryOrderStepOneResponse(

    @field:SerializedName("kode_do")
    val kodeDo: String,

    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String
) : Parcelable
