package com.android.burdacontractor.feature.deliveryorder.domain.usecase

import com.android.burdacontractor.core.domain.model.enums.CreatedByOrFor
import com.android.burdacontractor.core.domain.model.enums.DeliveryOrderStatus
import com.android.burdacontractor.core.domain.model.enums.SuratJalanStatus
import com.android.burdacontractor.core.domain.model.enums.SuratJalanTipe
import com.android.burdacontractor.feature.deliveryorder.domain.repository.IDeliveryOrderRepository
import com.android.burdacontractor.feature.suratjalan.domain.repository.ISuratJalanRepository
import javax.inject.Inject

class GetAllDeliveryOrderInteractor @Inject constructor(private val deliveryOrderRepository: IDeliveryOrderRepository):
    GetAllDeliveryOrderUseCase
{
    override fun execute(
        status: DeliveryOrderStatus,
        date_start: String?,
        date_end: String?,
        size: Int,
        search: String?,
        createdByOrFor: CreatedByOrFor
    )= deliveryOrderRepository.getAllDeliveryOrder(status, date_start, date_end, size, search, createdByOrFor)
}