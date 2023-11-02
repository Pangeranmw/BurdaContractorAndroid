package com.android.burdacontractor.feature.kendaraan.domain.usecase

import com.android.burdacontractor.feature.kendaraan.domain.repository.IKendaraanRepository
import java.io.File
import javax.inject.Inject

class UpdateKendaraanInteractor @Inject constructor(private val kendaraanRepository: IKendaraanRepository) :
    UpdateKendaraanUseCase {
    override suspend fun execute(
        id: String,
        gudangId: String,
        jenis: String,
        merk: String,
        platNomor: String,
        gambar: File?
    ) = kendaraanRepository.updateKendaraan(id, gudangId, jenis, merk, platNomor, gambar)
}