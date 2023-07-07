package com.android.burdacontractor.feature.deliveryorder.domain.usecase

import com.android.burdacontractor.feature.deliveryorder.domain.repository.IDeliveryOrderRepository
import javax.inject.Inject

class DeleteDeliveryOrderInteractor @Inject constructor(private val deliveryOrderRepository: IDeliveryOrderRepository):
    DeleteDeliveryOrderUseCase
{
    override suspend fun execute(deliveryOrderId: String)=deliveryOrderRepository.deleteDeliveryOrder(deliveryOrderId)
}