package com.android.burdacontractor.feature.proyek.domain.usecase

import com.android.burdacontractor.feature.proyek.domain.repository.ILogisticRepository
import javax.inject.Inject

class GetLogisticActiveSjDoLocationInteractor @Inject constructor(private val logisticRepository: ILogisticRepository) :
    GetLogisticActiveSjDoLocationUseCase {
    override suspend fun execute(id: String) = logisticRepository.getLogisticActiveSjDoLocation(id)
}