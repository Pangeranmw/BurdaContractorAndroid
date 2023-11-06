package com.android.burdacontractor.feature.logistic.domain.usecase

import com.android.burdacontractor.feature.logistic.domain.repository.ILogisticRepository
import javax.inject.Inject

class GetLogisticActiveSjDoLocationInteractor @Inject constructor(private val logisticRepository: ILogisticRepository) :
    GetLogisticActiveSjDoLocationUseCase {
    override suspend fun execute(id: String) = logisticRepository.getLogisticActiveSjDoLocation(id)
}