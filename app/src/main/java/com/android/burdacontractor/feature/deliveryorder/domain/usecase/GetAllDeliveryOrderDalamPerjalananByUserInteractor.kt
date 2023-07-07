package com.android.burdacontractor.feature.deliveryorder.domain.usecase

import com.android.burdacontractor.feature.deliveryorder.domain.repository.IDeliveryOrderRepository
import javax.inject.Inject

class GetAllDeliveryOrderDalamPerjalananByUserInteractor @Inject constructor(private val deliveryOrderRepository: IDeliveryOrderRepository):
    GetAllDeliveryOrderDalamPerjalananByUserUseCase
{
    override suspend fun execute()=deliveryOrderRepository.getAllDeliveryOrderDalamPerjalananByUser()
}