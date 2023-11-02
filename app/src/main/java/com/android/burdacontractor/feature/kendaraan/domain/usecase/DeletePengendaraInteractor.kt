package com.android.burdacontractor.feature.kendaraan.domain.usecase

import com.android.burdacontractor.feature.kendaraan.domain.repository.IKendaraanRepository
import javax.inject.Inject

class DeletePengendaraInteractor @Inject constructor(private val kendaraanRepository: IKendaraanRepository) :
    DeletePengendaraUseCase {
    override suspend fun execute(id: String) = kendaraanRepository.deletePengendara(id)
}