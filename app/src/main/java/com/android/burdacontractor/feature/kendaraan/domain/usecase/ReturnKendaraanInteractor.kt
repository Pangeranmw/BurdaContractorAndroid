package com.android.burdacontractor.feature.kendaraan.domain.usecase

import com.android.burdacontractor.feature.kendaraan.domain.repository.IKendaraanRepository
import javax.inject.Inject

class ReturnKendaraanInteractor @Inject constructor(private val kendaraanRepository: IKendaraanRepository) :
    ReturnKendaraanUseCase {
    override suspend fun execute(id: String) = kendaraanRepository.returnKendaraan(id)
}