package com.android.burdacontractor.feature.perusahaan.domain.usecase

import com.android.burdacontractor.feature.perusahaan.domain.repository.IPerusahaanRepository
import javax.inject.Inject

class GetAllPerusahaanInteractor @Inject constructor(private val perusahaanRepository: IPerusahaanRepository) :
    GetAllPerusahaanUseCase {
    override fun execute(
        size: Int,
        search: String?,
        filter: String?,
        coordinate: String?,
    ) = perusahaanRepository.getAllPerusahaan(size, search, filter, coordinate)
}