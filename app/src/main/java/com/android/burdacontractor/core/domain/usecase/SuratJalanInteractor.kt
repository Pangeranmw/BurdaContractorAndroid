package com.android.burdacontractor.core.domain.usecase

import com.android.burdacontractor.core.data.Resource
import com.android.burdacontractor.core.data.source.remote.response.AddSuratJalanResponse
import com.android.burdacontractor.core.domain.model.SuratJalanStatus
import com.android.burdacontractor.core.domain.model.SuratJalanTipe
import com.android.burdacontractor.core.domain.repository.ISuratJalanRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SuratJalanInteractor @Inject constructor(private val suratJalanRepository: ISuratJalanRepository):
    SuratJalanUseCase {
    override suspend fun getAllSuratJalan(
        tipe: SuratJalanTipe,
        status: SuratJalanStatus,
        date_start: String?,
        date_end: String?,
        size: Int,
        search: String?
    )= suratJalanRepository.getAllSuratJalan(tipe, status, date_start, date_end, size, search)

    override suspend fun getSuratJalanById(id: String) = suratJalanRepository.getSuratJalanById(id)

    override suspend fun addSuratJalanPengirimanGp(
        adminGudangId: String,
        logisticId: String,
        kendaraanId: String,
        peminjamanId: String
    )=suratJalanRepository.addSuratJalanPengirimanGp(adminGudangId, logisticId,kendaraanId,peminjamanId)

    override suspend fun addSuratJalanPengirimanPp(
        adminGudangId: String,
        logisticId: String,
        kendaraanId: String,
        peminjamanAsalId: String,
        peminjamanTujuanId: String
    )=suratJalanRepository.addSuratJalanPengirimanPp(adminGudangId, logisticId,kendaraanId,peminjamanAsalId,peminjamanTujuanId)

    override suspend fun addSuratJalanPengembalian(
        adminGudangId: String,
        logisticId: String,
        kendaraanId: String,
        pengembalianId: String
    )=suratJalanRepository.addSuratJalanPengembalian(adminGudangId, logisticId,kendaraanId,pengembalianId)

    override suspend fun updateSuratJalanPengirimanGp(
        adminGudangId: String,
        logisticId: String,
        kendaraanId: String,
        peminjamanId: String
    )=suratJalanRepository.updateSuratJalanPengirimanGp(adminGudangId, logisticId,kendaraanId,peminjamanId)

    override suspend fun updateSuratJalanPengirimanPp(
        adminGudangId: String,
        logisticId: String,
        kendaraanId: String,
        peminjamanAsalId: String,
        peminjamanTujuanId: String
    ): Flow<Resource<AddSuratJalanResponse>> {
        TODO("Not yet implemented")
    }
    override suspend fun updateSuratJalanPengembalian(
        adminGudangId: String,
        logisticId: String,
        kendaraanId: String,
        pengembalianId: String
    )=suratJalanRepository.updateSuratJalanPengembalian(adminGudangId, logisticId,kendaraanId,pengembalianId)

    override suspend fun deleteSuratJalan(suratJalanId: String)=suratJalanRepository.deleteSuratJalan(suratJalanId)
}