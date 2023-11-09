package com.android.burdacontractor.feature.suratjalan.data.source.remote.response

import android.os.Parcelable
import com.android.burdacontractor.feature.suratjalan.domain.model.SuratJalanDetailItem
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class SuratJalanDetailResponse(
	@field:SerializedName("error")
	val error: Boolean,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("surat_jalan")
	val suratJalan: SuratJalanDetailItem
) : Parcelable