package com.android.burdacontractor.feature.perusahaan.data.source.remote.response

import com.android.burdacontractor.feature.perusahaan.domain.model.PerusahaanById
import com.google.gson.annotations.SerializedName


data class GetPerusahaanByIdResponse(

	@field:SerializedName("perusahaan")
	val perusahaan: PerusahaanById,

	@field:SerializedName("error")
	val error: Boolean,

	@field:SerializedName("message")
	val message: String
)

