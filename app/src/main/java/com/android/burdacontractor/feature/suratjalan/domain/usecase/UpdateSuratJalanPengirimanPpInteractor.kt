package com.android.burdacontractor.feature.suratjalan.domain.usecase

import com.android.burdacontractor.feature.suratjalan.domain.repository.ISuratJalanRepository
import javax.inject.Inject

class UpdateSuratJalanPengirimanPpInteractor @Inject constructor(private val suratJalanRepository: ISuratJalanRepository):
    UpdateSuratJalanPengirimanPpUseCase
{
    override suspend fun execute(
        adminGudangId: String,
        logisticId: String,
        kendaraanId: String,
        peminjamanAsalId: String,
        peminjamanTujuanId: String
    ) = suratJalanRepository.updateSuratJalanPengirimanPp(adminGudangId, logisticId,kendaraanId,peminjamanAsalId,peminjamanTujuanId)
}