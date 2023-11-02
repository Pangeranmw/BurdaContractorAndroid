package com.android.burdacontractor.feature.kendaraan.domain.usecase

import com.android.burdacontractor.feature.kendaraan.domain.repository.IKendaraanRepository
import java.io.File
import javax.inject.Inject

class AddKendaraanInteractor @Inject constructor(private val kendaraanRepository: IKendaraanRepository) :
    AddKendaraanUseCase {
    override suspend fun execute(
        gudangId: String,
        jenis: String,
        merk: String,
        platNomor: String,
        gambar: File
    ) = kendaraanRepository.addKendaraan(gudangId, jenis, merk, platNomor, gambar)
}