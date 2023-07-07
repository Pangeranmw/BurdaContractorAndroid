package com.android.burdacontractor.feature.suratjalan.domain.usecase

import com.android.burdacontractor.core.data.Resource
import com.android.burdacontractor.feature.suratjalan.domain.model.AllSuratJalan
import kotlinx.coroutines.flow.Flow

interface GetAllSuratJalanDalamPerjalananByUserUseCase {
    suspend fun execute(): Flow<Resource<List<AllSuratJalan>>>
}