package com.android.burdacontractor.feature.gudang.domain.usecase

import com.android.burdacontractor.feature.gudang.domain.repository.IGudangRepository
import javax.inject.Inject

class GetStatistikDeliveryOrderByGudangInteractor @Inject constructor(private val gudangRepository: IGudangRepository) :
    GetStatistikDeliveryOrderByGudangUseCase {
    override suspend fun execute(
        id: String
    ) = gudangRepository.getStatistikDeliveryOrderByGudang(id)
}