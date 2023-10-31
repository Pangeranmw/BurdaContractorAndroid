package com.android.burdacontractor.feature.deliveryorder.domain.usecase

import com.android.burdacontractor.core.domain.model.enums.CreatedByOrFor
import com.android.burdacontractor.core.domain.model.enums.DeliveryOrderStatus
import com.android.burdacontractor.feature.deliveryorder.domain.repository.IDeliveryOrderRepository
import javax.inject.Inject

class GetAllDeliveryOrderInteractor @Inject constructor(private val deliveryOrderRepository: IDeliveryOrderRepository):
    GetAllDeliveryOrderUseCase
{
    override fun execute(
        status: DeliveryOrderStatus,
        dateStart: String?,
        dateEnd: String?,
        size: Int,
        search: String?,
        createdByOrFor: CreatedByOrFor
    ) = deliveryOrderRepository.getAllDeliveryOrder(
        status,
        dateStart,
        dateEnd,
        size,
        search,
        createdByOrFor
    )
}