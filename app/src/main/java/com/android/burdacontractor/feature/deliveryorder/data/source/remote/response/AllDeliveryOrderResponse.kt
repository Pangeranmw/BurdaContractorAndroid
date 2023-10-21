package com.android.burdacontractor.feature.deliveryorder.data.source.remote.response

import com.android.burdacontractor.feature.deliveryorder.domain.model.AllDeliveryOrder
import com.google.gson.annotations.SerializedName

data class AllDeliveryOrderResponse(

	@field:SerializedName("error")
	val error: Boolean,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("delivery_order")
	val deliveryOrder: List<AllDeliveryOrder>? = null
)
