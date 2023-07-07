package com.android.burdacontractor.feature.deliveryorder.domain.model

data class DataAllDeliveryOrderWithCount(
    val count: Int? = null,
    val deliveryOrder: List<AllDeliveryOrder>? = null
)