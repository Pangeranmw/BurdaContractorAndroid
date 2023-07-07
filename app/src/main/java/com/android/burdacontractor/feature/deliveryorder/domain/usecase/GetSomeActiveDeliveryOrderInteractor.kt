package com.android.burdacontractor.feature.deliveryorder.domain.usecase

import com.android.burdacontractor.core.domain.model.enums.SuratJalanTipe
import com.android.burdacontractor.feature.deliveryorder.domain.repository.IDeliveryOrderRepository
import com.android.burdacontractor.feature.suratjalan.domain.repository.ISuratJalanRepository
import javax.inject.Inject

class GetSomeActiveDeliveryOrderInteractor @Inject constructor(private val deliveryOrderRepository: IDeliveryOrderRepository):
    GetSomeActiveDeliveryOrderUseCase
{
    override suspend fun execute(size: Int) = deliveryOrderRepository.getSomeActiveDeliveryOrder(size)
}