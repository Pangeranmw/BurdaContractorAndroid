package com.android.burdacontractor.feature.deliveryorder.domain.usecase

import com.android.burdacontractor.feature.deliveryorder.data.source.remote.request.AddDeliveryOrderStepOneBody
import com.android.burdacontractor.feature.deliveryorder.domain.repository.IDeliveryOrderRepository
import javax.inject.Inject

class AddDeliveryOrderStepOneInteractor @Inject constructor(private val deliveryOrderRepository: IDeliveryOrderRepository) :
    AddDeliveryOrderStepOneUseCase {
    override suspend fun execute(
        addDeliveryOrderStepOneBody: AddDeliveryOrderStepOneBody
    ) = deliveryOrderRepository.addDeliveryOrderStepOne(addDeliveryOrderStepOneBody)
}