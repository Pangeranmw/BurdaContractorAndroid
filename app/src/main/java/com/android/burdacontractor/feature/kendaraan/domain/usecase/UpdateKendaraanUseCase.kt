package com.android.burdacontractor.feature.kendaraan.domain.usecase

import com.android.burdacontractor.core.data.Resource
import kotlinx.coroutines.flow.Flow
import java.io.File

interface UpdateKendaraanUseCase {
    suspend fun execute(
        id: String,
        gudangId: String,
        jenis: String,
        merk: String,
        platNomor: String,
        gambar: File?
    ): Flow<Resource<String>>
}