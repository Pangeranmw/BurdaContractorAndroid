package com.android.burdacontractor.feature.deliveryorder.domain.usecase

import com.android.burdacontractor.feature.deliveryorder.domain.repository.IDeliveryOrderRepository
import javax.inject.Inject

class MarkCompleteDeliveryOrderInteractor @Inject constructor(private val deliveryOrderRepository: IDeliveryOrderRepository) :
    MarkCompleteDeliveryOrderUseCase {
    override suspend fun execute(deliveryOrderId: String) =
        deliveryOrderRepository.markCompleteDeliveryOrder(deliveryOrderId)
}