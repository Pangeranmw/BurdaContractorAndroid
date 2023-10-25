package com.android.burdacontractor.feature.kendaraan.domain.usecase

import com.android.burdacontractor.feature.kendaraan.domain.repository.IKendaraanRepository
import javax.inject.Inject

class GetKendaraanByIdInteractor @Inject constructor(private val kendaraanRepository: IKendaraanRepository) :
    GetKendaraanByIdUseCase {
    override suspend fun execute(id: String) = kendaraanRepository.getKendaraanById(id)
}