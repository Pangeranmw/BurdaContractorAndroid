package com.android.burdacontractor.feature.deliveryorder.data.source.remote.response

import com.android.burdacontractor.feature.deliveryorder.domain.model.DeliveryOrderDetailItem
import com.google.gson.annotations.SerializedName

data class DeliveryOrderDetailResponse(

	@field:SerializedName("error")
	val error: Boolean,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("delivery_order")
	val deliveryOrder: DeliveryOrderDetailItem
)