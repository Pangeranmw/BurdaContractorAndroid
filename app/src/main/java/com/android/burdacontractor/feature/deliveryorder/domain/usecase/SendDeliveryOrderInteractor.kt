package com.android.burdacontractor.feature.deliveryorder.domain.usecase

import com.android.burdacontractor.feature.deliveryorder.domain.repository.IDeliveryOrderRepository
import javax.inject.Inject

class SendDeliveryOrderInteractor @Inject constructor(private val deliveryOrderRepository: IDeliveryOrderRepository) :
    SendDeliveryOrderUseCase {
    override suspend fun execute(deliveryOrderId: String) =
        deliveryOrderRepository.sendDeliveryOrder(deliveryOrderId)
}