package com.android.burdacontractor.feature.suratjalan.domain.usecase

import com.android.burdacontractor.feature.suratjalan.domain.repository.ISuratJalanRepository
import javax.inject.Inject

class UpdateSuratJalanPengembalianInteractor @Inject constructor(private val suratJalanRepository: ISuratJalanRepository):
    UpdateSuratJalanPengembalianUseCase
{
    override suspend fun execute(
        adminGudangId: String,
        logisticId: String,
        kendaraanId: String,
        pengembalianId: String
    )=suratJalanRepository.updateSuratJalanPengembalian(adminGudangId, logisticId,kendaraanId,pengembalianId)

}