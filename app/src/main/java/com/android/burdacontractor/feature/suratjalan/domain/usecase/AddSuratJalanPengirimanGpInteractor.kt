package com.android.burdacontractor.feature.suratjalan.domain.usecase

import com.android.burdacontractor.feature.suratjalan.domain.repository.ISuratJalanRepository
import javax.inject.Inject

class AddSuratJalanPengirimanGpInteractor @Inject constructor(private val suratJalanRepository: ISuratJalanRepository):
    AddSuratJalanPengirimanGpUseCase
{
    override suspend fun execute(
        adminGudangId: String,
        logisticId: String,
        kendaraanId: String,
        peminjamanId: String
    )=suratJalanRepository.addSuratJalanPengirimanGp(adminGudangId, logisticId,kendaraanId,peminjamanId)
}