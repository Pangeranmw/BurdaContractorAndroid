package com.android.burdacontractor.feature.logistic.domain.usecase

import com.android.burdacontractor.feature.logistic.domain.repository.ILogisticRepository
import javax.inject.Inject

class GetAllLogisticInteractor @Inject constructor(private val logisticRepository: ILogisticRepository) :
    GetAllLogisticUseCase {
    override fun execute(
        search: String?,
        coordinate: String?,
        size: Int,
    ) = logisticRepository.getAllLogistic(search)
}