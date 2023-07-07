package com.android.burdacontractor.feature.deliveryorder.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class AllDeliveryOrderWithCountResponse(

	@field:SerializedName("data")
	val data: DataAllDeliveryOrderWithCountItem? = null,

	@field:SerializedName("error")
	val error: Boolean,

	@field:SerializedName("message")
	val message: String
)

data class DataAllDeliveryOrderWithCountItem(

	@field:SerializedName("count")
	val count: Int? = null,

	@field:SerializedName("delivery_order")
	val suratJalan: List<DeliveryOrderItem>? = null
)
