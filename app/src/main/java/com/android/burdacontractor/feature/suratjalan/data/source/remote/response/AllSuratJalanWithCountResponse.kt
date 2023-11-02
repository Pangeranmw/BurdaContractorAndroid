package com.android.burdacontractor.feature.suratjalan.data.source.remote.response

import com.android.burdacontractor.feature.suratjalan.domain.model.AllSuratJalan
import com.google.gson.annotations.SerializedName

data class AllSuratJalanWithCountResponse(

	@field:SerializedName("data")
	val data: DataAllSuratJalanWithCountItem? = null,

	@field:SerializedName("error")
	val error: Boolean,

	@field:SerializedName("message")
	val message: String
)

data class DataAllSuratJalanWithCountItem(

	@field:SerializedName("count")
	val count: Int? = null,

	@field:SerializedName("surat_jalan")
	val suratJalan: List<AllSuratJalan>? = null
)
