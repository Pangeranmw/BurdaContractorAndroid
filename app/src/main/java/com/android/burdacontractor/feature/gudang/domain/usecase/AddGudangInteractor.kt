package com.android.burdacontractor.feature.gudang.domain.usecase

import com.android.burdacontractor.feature.gudang.domain.repository.IGudangRepository
import java.io.File
import javax.inject.Inject

class AddGudangInteractor @Inject constructor(private val gudangRepository: IGudangRepository) :
    AddGudangUseCase {
    override suspend fun execute(
        nama: String,
        alamat: String,
        provinsi: String,
        kota: String,
        latitude: String,
        longitude: String,
        gambar: File,
    ) = gudangRepository.addGudang(nama, alamat, provinsi, kota, latitude, longitude, gambar)
}