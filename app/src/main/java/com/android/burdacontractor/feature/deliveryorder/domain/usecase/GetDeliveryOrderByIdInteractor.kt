package com.android.burdacontractor.feature.deliveryorder.domain.usecase

import com.android.burdacontractor.feature.deliveryorder.domain.repository.IDeliveryOrderRepository
import com.android.burdacontractor.feature.suratjalan.domain.repository.ISuratJalanRepository
import javax.inject.Inject

class GetDeliveryOrderByIdInteractor @Inject constructor(private val deliveryOrderRepository: IDeliveryOrderRepository):
    GetDeliveryOrderByIdUseCase
{
    override suspend fun execute(id: String) = deliveryOrderRepository.getDeliveryOrderById(id)
}