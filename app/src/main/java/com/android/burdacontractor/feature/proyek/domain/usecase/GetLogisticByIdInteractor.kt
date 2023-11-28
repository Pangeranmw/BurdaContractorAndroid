package com.android.burdacontractor.feature.proyek.domain.usecase

import com.android.burdacontractor.feature.proyek.domain.repository.ILogisticRepository
import javax.inject.Inject

class GetLogisticByIdInteractor @Inject constructor(private val logisticRepository: ILogisticRepository) :
    GetLogisticByIdUseCase {
    override suspend fun execute(id: String) = logisticRepository.getLogisticById(id)
}