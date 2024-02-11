package com.android.burdacontractor.feature.logistic.data.source.remote.response

import android.os.Parcelable
import com.android.burdacontractor.feature.logistic.domain.model.ActiveSjDoLocation
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class GetActiveSjDoLocationByLogisticResponse(

    @field:SerializedName("active_location")
	val activeLocation: List<ActiveSjDoLocation>,

    @field:SerializedName("error")
	val error: Boolean,

    @field:SerializedName("message")
	val message: String
) : Parcelable

