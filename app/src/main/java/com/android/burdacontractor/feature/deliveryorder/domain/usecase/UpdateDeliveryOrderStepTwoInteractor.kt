package com.android.burdacontractor.feature.deliveryorder.domain.usecase

import com.android.burdacontractor.feature.deliveryorder.data.source.remote.request.AddUpdateDeliveryOrderStepTwoBody
import com.android.burdacontractor.feature.deliveryorder.domain.repository.IDeliveryOrderRepository
import javax.inject.Inject

class UpdateDeliveryOrderStepTwoInteractor @Inject constructor(private val deliveryOrderRepository: IDeliveryOrderRepository) :
    UpdateDeliveryOrderStepTwoUseCase {
    override suspend fun execute(
        id: String,
        addUpdateDeliveryOrderStepTwoBody: AddUpdateDeliveryOrderStepTwoBody
    ) = deliveryOrderRepository.updateDeliveryOrderStepTwo(id, addUpdateDeliveryOrderStepTwoBody)
}