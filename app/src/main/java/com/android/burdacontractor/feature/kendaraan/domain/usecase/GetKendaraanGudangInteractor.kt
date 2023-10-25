package com.android.burdacontractor.feature.kendaraan.domain.usecase

import com.android.burdacontractor.feature.kendaraan.domain.repository.IKendaraanRepository
import javax.inject.Inject

class GetKendaraanGudangInteractor @Inject constructor(private val kendaraanRepository: IKendaraanRepository) :
    GetKendaraanGudangUseCase {
    override suspend fun execute() = kendaraanRepository.getKendaraanGudang()
}