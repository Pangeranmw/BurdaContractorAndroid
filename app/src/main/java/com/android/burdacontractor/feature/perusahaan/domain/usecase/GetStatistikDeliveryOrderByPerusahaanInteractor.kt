package com.android.burdacontractor.feature.perusahaan.domain.usecase

import com.android.burdacontractor.feature.perusahaan.domain.repository.IPerusahaanRepository
import javax.inject.Inject

class GetStatistikDeliveryOrderByPerusahaanInteractor @Inject constructor(private val perusahaanRepository: IPerusahaanRepository) :
    GetStatistikDeliveryOrderByPerusahaanUseCase {
    override suspend fun execute(
        id: String
    ) = perusahaanRepository.getStatistikDeliveryOrderByPerusahaan(id)
}