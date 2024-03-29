package com.android.burdacontractor.feature.deliveryorder.domain.usecase

import com.android.burdacontractor.feature.deliveryorder.data.source.remote.request.AddUpdateDeliveryOrderStepOneBody
import com.android.burdacontractor.feature.deliveryorder.domain.repository.IDeliveryOrderRepository
import javax.inject.Inject

class AddDeliveryOrderStepOneInteractor @Inject constructor(private val deliveryOrderRepository: IDeliveryOrderRepository) :
    AddDeliveryOrderStepOneUseCase {
    override suspend fun execute(
        addUpdateDeliveryOrderStepOneBody: AddUpdateDeliveryOrderStepOneBody
    ) = deliveryOrderRepository.addDeliveryOrderStepOne(addUpdateDeliveryOrderStepOneBody)
}