package com.android.burdacontractor.feature.kendaraan.domain.usecase

import com.android.burdacontractor.feature.kendaraan.domain.repository.IKendaraanRepository
import javax.inject.Inject

class GetAllKendaraanInteractor @Inject constructor(private val kendaraanRepository: IKendaraanRepository) :
    GetAllKendaraanUseCase {
    override fun execute(
        size: Int,
        filter: String?,
        gudang: String?,
        status: String?,
        search: String?,
    ) = kendaraanRepository.getAllKendaraan(size, filter, gudang, status, search)
}