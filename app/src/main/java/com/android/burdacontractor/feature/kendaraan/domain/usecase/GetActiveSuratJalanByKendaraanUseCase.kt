package com.android.burdacontractor.feature.kendaraan.domain.usecase

import com.android.burdacontractor.core.data.Resource
import com.android.burdacontractor.core.domain.model.enums.SuratJalanTipe
import com.android.burdacontractor.feature.suratjalan.domain.model.AllSuratJalan
import kotlinx.coroutines.flow.Flow

interface GetActiveSuratJalanByKendaraanUseCase {
    suspend fun execute(
        id: String,
        tipe: SuratJalanTipe,
    ): Flow<Resource<List<AllSuratJalan>>>
}