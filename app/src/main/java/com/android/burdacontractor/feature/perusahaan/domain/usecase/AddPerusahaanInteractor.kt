package com.android.burdacontractor.feature.perusahaan.domain.usecase

import com.android.burdacontractor.feature.perusahaan.domain.repository.IPerusahaanRepository
import java.io.File
import javax.inject.Inject

class AddPerusahaanInteractor @Inject constructor(private val perusahaanRepository: IPerusahaanRepository) :
    AddPerusahaanUseCase {
    override suspend fun execute(
        nama: String,
        alamat: String,
        provinsi: String,
        kota: String,
        latitude: String,
        longitude: String,
        gambar: File,
    ) = perusahaanRepository.addPerusahaan(
        nama,
        alamat,
        provinsi,
        kota,
        latitude,
        longitude,
        gambar
    )
}