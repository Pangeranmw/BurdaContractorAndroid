package com.android.burdacontractor.core.domain.model

import android.os.Parcelable
import com.android.burdacontractor.feature.deliveryorder.domain.model.PreOrder
import kotlinx.parcelize.Parcelize

@Parcelize
data class DeliveryOrderDetail(
    var deliveryOrder: DeliveryOrder? = null,
    var preOrder: List<PreOrder?>? = null,
): Parcelable

