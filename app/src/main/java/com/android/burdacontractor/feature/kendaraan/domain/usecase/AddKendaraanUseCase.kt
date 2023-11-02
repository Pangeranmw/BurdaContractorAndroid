package com.android.burdacontractor.feature.kendaraan.domain.usecase

import com.android.burdacontractor.core.data.Resource
import kotlinx.coroutines.flow.Flow
import java.io.File

interface AddKendaraanUseCase {
    suspend fun execute(
        gudangId: String,
        jenis: String,
        merk: String,
        platNomor: String,
        gambar: File
    ): Flow<Resource<String>>
}