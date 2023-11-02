package com.android.burdacontractor.feature.kendaraan.domain.usecase

import com.android.burdacontractor.feature.kendaraan.domain.repository.IKendaraanRepository
import javax.inject.Inject

class DeleteKendaraanInteractor @Inject constructor(private val kendaraanRepository: IKendaraanRepository) :
    DeleteKendaraanUseCase {
    override suspend fun execute(id: String) = kendaraanRepository.deleteKendaraan(id)
}