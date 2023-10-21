package com.android.burdacontractor.feature.perusahaan.domain.usecase

import com.android.burdacontractor.feature.perusahaan.domain.repository.IPerusahaanRepository
import javax.inject.Inject

class GetPerusahaanByIdInteractor @Inject constructor(private val perusahaanRepository: IPerusahaanRepository) :
    GetPerusahaanByIdUseCase {
    override suspend fun execute(id: String) = perusahaanRepository.getPerusahaanById(id)
}