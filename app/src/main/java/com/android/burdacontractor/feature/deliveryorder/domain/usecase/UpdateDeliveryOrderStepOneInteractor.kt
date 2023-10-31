package com.android.burdacontractor.feature.deliveryorder.domain.usecase

import com.android.burdacontractor.feature.deliveryorder.data.source.remote.request.AddUpdateDeliveryOrderStepOneBody
import com.android.burdacontractor.feature.deliveryorder.domain.repository.IDeliveryOrderRepository
import javax.inject.Inject

class UpdateDeliveryOrderStepOneInteractor @Inject constructor(private val deliveryOrderRepository: IDeliveryOrderRepository) :
    UpdateDeliveryOrderStepOneUseCase {
    override suspend fun execute(
        id: String,
        addUpdateDeliveryOrderStepOneBody: AddUpdateDeliveryOrderStepOneBody
    ) = deliveryOrderRepository.updateDeliveryOrderStepOne(id, addUpdateDeliveryOrderStepOneBody)
}