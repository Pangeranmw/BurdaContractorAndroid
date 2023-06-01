package com.android.burdacontractor.feature.suratjalan.domain.usecase

import com.android.burdacontractor.feature.suratjalan.domain.repository.ISuratJalanRepository
import javax.inject.Inject

class AddSuratJalanPengirimanPpInteractor @Inject constructor(private val suratJalanRepository: ISuratJalanRepository):
    AddSuratJalanPengirimanPpUseCase
{
    override suspend fun execute(
        adminGudangId: String,
        logisticId: String,
        kendaraanId: String,
        peminjamanAsalId: String,
        peminjamanTujuanId: String
    )=suratJalanRepository.addSuratJalanPengirimanPp(adminGudangId, logisticId,kendaraanId,peminjamanAsalId,peminjamanTujuanId)
}