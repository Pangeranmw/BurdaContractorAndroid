package com.android.burdacontractor.feature.suratjalan.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class StatisticCountTitleResponse(

	@field:SerializedName("error")
	val error: Boolean,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("statistic")
	val statisticCountTitleItems: List<StatisticCountTitleItem>
)

data class StatisticCountTitleItem(

	@field:SerializedName("count")
	val count: Int,

	@field:SerializedName("title")
	val title: String
)
