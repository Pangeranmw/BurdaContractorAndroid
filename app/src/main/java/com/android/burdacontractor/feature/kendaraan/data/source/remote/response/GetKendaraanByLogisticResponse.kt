package com.android.burdacontractor.feature.kendaraan.data.source.remote.response

import com.android.burdacontractor.feature.kendaraan.domain.model.KendaraanByLogistic
import com.google.gson.annotations.SerializedName

data class GetKendaraanByLogisticResponse(
	@field:SerializedName("kendaraaan")
	val kendaraaan: KendaraanByLogistic? = null,

	@field:SerializedName("error")
	val error: Boolean,

	@field:SerializedName("message")
	val message: String
)