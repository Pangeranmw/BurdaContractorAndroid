package com.android.burdacontractor.feature.suratjalan.data.source.remote.response

import com.android.burdacontractor.feature.suratjalan.domain.model.AllSuratJalan
import com.google.gson.annotations.SerializedName

data class AllSuratJalanResponse(

	@field:SerializedName("error")
	val error: Boolean,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("surat_jalan")
	val suratJalan: List<AllSuratJalan>? = null
)
