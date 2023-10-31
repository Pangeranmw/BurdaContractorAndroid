package com.android.burdacontractor.feature.deliveryorder.domain.usecase

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.android.burdacontractor.core.domain.model.enums.CreatedByOrFor
import com.android.burdacontractor.core.domain.model.enums.DeliveryOrderStatus
import com.android.burdacontractor.feature.deliveryorder.domain.model.AllDeliveryOrder

interface GetAllDeliveryOrderUseCase {
    fun execute(
        status: DeliveryOrderStatus,
        dateStart: String? = null,
        dateEnd: String? = null,
        size: Int = 5,
        search: String? = null,
        createdByOrFor: CreatedByOrFor,
    ): LiveData<PagingData<AllDeliveryOrder>>
}