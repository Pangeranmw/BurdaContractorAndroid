package com.android.burdacontractor.feature.suratjalan.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class StatistikMenungguSuratJalanResponse(

	@field:SerializedName("error")
	val error: Boolean,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("statistik_menunggu_surat_jalan")
	val statistikMenungguSuratJalan: List<StatistikMenungguSuratJalanItem>
)

data class StatistikMenungguSuratJalanItem(

	@field:SerializedName("count")
	val count: Int,

	@field:SerializedName("title")
	val title: String
)
