package com.android.burdacontractor.feature.gudang.domain.usecase

import com.android.burdacontractor.core.data.Resource
import com.android.burdacontractor.feature.suratjalan.domain.model.AllSuratJalan
import kotlinx.coroutines.flow.Flow

interface GetActiveSuratJalanByGudangUseCase {
    suspend fun execute(
        id: String,
        tipe: String,
    ): Flow<Resource<List<AllSuratJalan>>>
}