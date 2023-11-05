package com.android.burdacontractor.feature.perusahaan.domain.usecase

import com.android.burdacontractor.core.data.Resource
import kotlinx.coroutines.flow.Flow
import java.io.File

interface AddPerusahaanUseCase {
    suspend fun execute(
        nama: String,
        alamat: String,
        provinsi: String,
        kota: String,
        latitude: String,
        longitude: String,
        gambar: File,
    ): Flow<Resource<String>>
}