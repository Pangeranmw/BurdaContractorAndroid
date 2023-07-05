package com.android.burdacontractor.core.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class CountActiveResponse(
	@field:SerializedName("error")
	val error: Boolean,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("total_active")
	val totalActive: Int
)