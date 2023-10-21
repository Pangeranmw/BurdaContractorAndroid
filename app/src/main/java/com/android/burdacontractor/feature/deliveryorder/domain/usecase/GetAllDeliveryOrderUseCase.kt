package com.android.burdacontractor.feature.deliveryorder.domain.usecase

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.android.burdacontractor.core.domain.model.enums.CreatedByOrFor
import com.android.burdacontractor.core.domain.model.enums.DeliveryOrderStatus
import com.android.burdacontractor.feature.deliveryorder.domain.model.AllDeliveryOrder

interface GetAllDeliveryOrderUseCase {
    fun execute(
        status: DeliveryOrderStatus,
        date_start: String? = null,
        date_end: String? = null,
        size: Int = 5,
        search: String? = null,
        createdByOrFor: CreatedByOrFor,
    ): LiveData<PagingData<AllDeliveryOrder>>
}