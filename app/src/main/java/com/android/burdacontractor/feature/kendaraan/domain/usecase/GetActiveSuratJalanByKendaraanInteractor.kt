package com.android.burdacontractor.feature.kendaraan.domain.usecase

import com.android.burdacontractor.core.domain.model.enums.SuratJalanTipe
import com.android.burdacontractor.feature.kendaraan.domain.repository.IKendaraanRepository
import javax.inject.Inject

class GetActiveSuratJalanByKendaraanInteractor @Inject constructor(private val kendaraanRepository: IKendaraanRepository) :
    GetActiveSuratJalanByKendaraanUseCase {
    override suspend fun execute(
        id: String,
        tipe: SuratJalanTipe,
    ) = kendaraanRepository.getActiveSuratJalanByKendaraan(id, tipe)
}