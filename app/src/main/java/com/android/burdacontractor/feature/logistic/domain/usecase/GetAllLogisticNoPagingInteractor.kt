package com.android.burdacontractor.feature.logistic.domain.usecase

import com.android.burdacontractor.feature.logistic.domain.repository.ILogisticRepository
import javax.inject.Inject

class GetAllLogisticNoPagingInteractor @Inject constructor(private val logisticRepository: ILogisticRepository) :
    GetAllLogisticNoPagingUseCase {
    override suspend fun execute() = logisticRepository.getAllLogisticNoPaging()
}