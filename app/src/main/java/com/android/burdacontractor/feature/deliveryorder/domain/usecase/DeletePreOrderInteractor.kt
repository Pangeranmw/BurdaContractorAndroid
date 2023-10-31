package com.android.burdacontractor.feature.deliveryorder.domain.usecase

import com.android.burdacontractor.feature.deliveryorder.domain.repository.IDeliveryOrderRepository
import javax.inject.Inject

class DeletePreOrderInteractor @Inject constructor(private val deliveryOrderRepository: IDeliveryOrderRepository) :
    DeletePreOrderUseCase {
    override suspend fun execute(preOrderId: String) =
        deliveryOrderRepository.deletePreOrder(preOrderId)
}