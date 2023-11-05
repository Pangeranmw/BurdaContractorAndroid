package com.android.burdacontractor.feature.gudang.domain.usecase

import com.android.burdacontractor.feature.gudang.domain.repository.IGudangRepository
import java.io.File
import javax.inject.Inject

class UpdateGudangInteractor @Inject constructor(private val gudangRepository: IGudangRepository) :
    UpdateGudangUseCase {
    override suspend fun execute(
        id: String,
        nama: String,
        alamat: String,
        provinsi: String,
        kota: String,
        latitude: String,
        longitude: String,
        gambar: File?,
    ) = gudangRepository.updateGudang(id, nama, alamat, provinsi, kota, latitude, longitude, gambar)
}