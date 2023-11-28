package com.android.burdacontractor.feature.suratjalan.domain.usecase

import com.android.burdacontractor.core.data.Resource
import com.android.burdacontractor.feature.suratjalan.domain.model.PenggunaanSuratJalan
import kotlinx.coroutines.flow.Flow

interface GetAvailablePenggunaanByProyekUseCase {
    suspend fun execute(tipe: String, proyekId: String): Flow<Resource<List<PenggunaanSuratJalan>>>
}