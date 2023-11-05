package com.android.burdacontractor.feature.perusahaan.domain.usecase

import com.android.burdacontractor.feature.perusahaan.domain.repository.IPerusahaanRepository
import java.io.File
import javax.inject.Inject

class UpdatePerusahaanInteractor @Inject constructor(private val perusahaanRepository: IPerusahaanRepository) :
    UpdatePerusahaanUseCase {
    override suspend fun execute(
        id: String,
        nama: String,
        alamat: String,
        provinsi: String,
        kota: String,
        latitude: String,
        longitude: String,
        gambar: File?,
    ) = perusahaanRepository.updatePerusahaan(
        id,
        nama,
        alamat,
        provinsi,
        kota,
        latitude,
        longitude,
        gambar
    )
}