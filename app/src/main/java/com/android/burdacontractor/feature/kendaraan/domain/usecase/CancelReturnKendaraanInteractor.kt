package com.android.burdacontractor.feature.kendaraan.domain.usecase

import com.android.burdacontractor.feature.kendaraan.domain.repository.IKendaraanRepository
import javax.inject.Inject

class CancelReturnKendaraanInteractor @Inject constructor(private val kendaraanRepository: IKendaraanRepository) :
    CancelReturnKendaraanUseCase {
    override suspend fun execute(id: String) = kendaraanRepository.cancelReturnKendaraan(id)
}