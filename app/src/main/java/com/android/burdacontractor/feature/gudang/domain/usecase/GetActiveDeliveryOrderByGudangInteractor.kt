package com.android.burdacontractor.feature.gudang.domain.usecase

import com.android.burdacontractor.feature.gudang.domain.repository.IGudangRepository
import javax.inject.Inject

class GetActiveDeliveryOrderByGudangInteractor @Inject constructor(private val gudangRepository: IGudangRepository) :
    GetActiveDeliveryOrderByGudangUseCase {
    override suspend fun execute(
        id: String
    ) = gudangRepository.getActiveDeliveryOrderByGudang(id)
}