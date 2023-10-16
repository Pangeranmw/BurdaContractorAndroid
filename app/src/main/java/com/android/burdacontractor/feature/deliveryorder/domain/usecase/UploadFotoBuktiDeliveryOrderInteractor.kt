package com.android.burdacontractor.feature.deliveryorder.domain.usecase

import com.android.burdacontractor.feature.deliveryorder.domain.repository.IDeliveryOrderRepository
import java.io.File
import javax.inject.Inject

class UploadFotoBuktiDeliveryOrderInteractor @Inject constructor(private val deliveryOrderRepository: IDeliveryOrderRepository):
    UploadFotoBuktiDeliveryOrderUseCase
{
    override suspend fun execute(
        id: String,
        fotoBukti: File
    ) = deliveryOrderRepository.uploadFotoBuktiDeliveryOrder(id, fotoBukti)
}