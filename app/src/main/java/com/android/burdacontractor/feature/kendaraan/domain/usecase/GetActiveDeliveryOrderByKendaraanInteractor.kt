package com.android.burdacontractor.feature.kendaraan.domain.usecase

import com.android.burdacontractor.feature.kendaraan.domain.repository.IKendaraanRepository
import javax.inject.Inject

class GetActiveDeliveryOrderByKendaraanInteractor @Inject constructor(private val kendaraanRepository: IKendaraanRepository) :
    GetActiveDeliveryOrderByKendaraanUseCase {
    override suspend fun execute(
        id: String,
    ) = kendaraanRepository.getActiveDeliveryOrderByKendaraan(id)
}