package com.android.burdacontractor.feature.perusahaan.domain.usecase

import com.android.burdacontractor.feature.perusahaan.domain.repository.IPerusahaanRepository
import javax.inject.Inject

class DeletePerusahaanInteractor @Inject constructor(private val perusahaanRepository: IPerusahaanRepository) :
    DeletePerusahaanUseCase {
    override suspend fun execute(
        id: String
    ) = perusahaanRepository.deletePerusahaan(id)
}