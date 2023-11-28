package com.android.burdacontractor.feature.suratjalan.domain.usecase

import com.android.burdacontractor.core.data.Resource
import com.android.burdacontractor.feature.suratjalan.domain.model.PeminjamanSuratJalan
import kotlinx.coroutines.flow.Flow

interface GetAvailablePeminjamanByProyekUseCase {
    suspend fun execute(tipe: String, proyekId: String): Flow<Resource<List<PeminjamanSuratJalan>>>
}