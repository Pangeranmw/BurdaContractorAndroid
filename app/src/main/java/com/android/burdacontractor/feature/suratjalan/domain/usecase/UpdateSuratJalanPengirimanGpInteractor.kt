package com.android.burdacontractor.feature.suratjalan.domain.usecase

import com.android.burdacontractor.feature.suratjalan.domain.repository.ISuratJalanRepository
import javax.inject.Inject

class UpdateSuratJalanPengirimanGpInteractor @Inject constructor(private val suratJalanRepository: ISuratJalanRepository):
    UpdateSuratJalanPengirimanGpUseCase
{
    override suspend fun execute(
        adminGudangId: String,
        logisticId: String,
        kendaraanId: String,
        peminjamanId: String
    )=suratJalanRepository.updateSuratJalanPengirimanGp(adminGudangId, logisticId,kendaraanId,peminjamanId)

}