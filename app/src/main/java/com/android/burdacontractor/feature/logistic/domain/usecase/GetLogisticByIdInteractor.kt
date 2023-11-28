package com.android.burdacontractor.feature.logistic.domain.usecase

import com.android.burdacontractor.feature.proyek.domain.repository.ILogisticRepository
import com.android.burdacontractor.feature.proyek.domain.usecase.GetLogisticByIdUseCase
import javax.inject.Inject

class GetLogisticByIdInteractor @Inject constructor(private val logisticRepository: ILogisticRepository) :
    GetLogisticByIdUseCase {
    override suspend fun execute(id: String) = logisticRepository.getLogisticById(id)
}