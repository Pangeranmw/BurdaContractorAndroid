package com.android.burdacontractor.feature.suratjalan.domain.usecase

import com.android.burdacontractor.core.data.Resource
import com.android.burdacontractor.core.domain.model.Proyek
import kotlinx.coroutines.flow.Flow

interface GetAvailableProyekBySuratJalanTypeUseCase {
    suspend fun execute(tipe: String): Flow<Resource<List<Proyek>>>
}