package com.android.burdacontractor.feature.perusahaan.domain.usecase

import com.android.burdacontractor.feature.perusahaan.domain.repository.IPerusahaanRepository
import javax.inject.Inject

class GetPerusahaanProvinsiInteractor @Inject constructor(private val perusahaanRepository: IPerusahaanRepository) :
    GetPerusahaanProvinsiUseCase {
    override suspend fun execute() = perusahaanRepository.getPerusahaanProvinsi()
}