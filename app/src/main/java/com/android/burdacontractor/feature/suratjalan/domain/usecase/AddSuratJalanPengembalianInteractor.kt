package com.android.burdacontractor.feature.suratjalan.domain.usecase

import com.android.burdacontractor.feature.suratjalan.domain.repository.ISuratJalanRepository
import javax.inject.Inject

class AddSuratJalanPengembalianInteractor @Inject constructor(private val suratJalanRepository: ISuratJalanRepository):
    AddSuratJalanPengembalianUseCase
{
    override suspend fun execute(
        adminGudangId: String,
        logisticId: String,
        kendaraanId: String,
        pengembalianId: String
    )=suratJalanRepository.addSuratJalanPengembalian(adminGudangId, logisticId,kendaraanId,pengembalianId)
}