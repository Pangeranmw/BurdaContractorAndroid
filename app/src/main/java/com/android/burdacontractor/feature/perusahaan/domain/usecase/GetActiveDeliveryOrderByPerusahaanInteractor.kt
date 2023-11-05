package com.android.burdacontractor.feature.perusahaan.domain.usecase

import com.android.burdacontractor.feature.perusahaan.domain.repository.IPerusahaanRepository
import javax.inject.Inject

class GetActiveDeliveryOrderByPerusahaanInteractor @Inject constructor(private val perusahaanRepository: IPerusahaanRepository) :
    GetActiveDeliveryOrderByPerusahaanUseCase {
    override suspend fun execute(
        id: String
    ) = perusahaanRepository.getActiveDeliveryOrderByPerusahaan(id)
}