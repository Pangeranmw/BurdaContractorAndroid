package com.android.burdacontractor.feature.deliveryorder.domain.usecase

import com.android.burdacontractor.feature.deliveryorder.data.source.remote.request.AddUpdateDeliveryOrderStepTwoBody
import com.android.burdacontractor.feature.deliveryorder.domain.repository.IDeliveryOrderRepository
import javax.inject.Inject

class AddDeliveryOrderStepTwoInteractor @Inject constructor(private val deliveryOrderRepository: IDeliveryOrderRepository) :
    AddDeliveryOrderStepTwoUseCase {
    override suspend fun execute(
        addUpdateDeliveryOrderStepTwoBody: AddUpdateDeliveryOrderStepTwoBody
    ) = deliveryOrderRepository.addDeliveryOrderStepTwo(addUpdateDeliveryOrderStepTwoBody)
}