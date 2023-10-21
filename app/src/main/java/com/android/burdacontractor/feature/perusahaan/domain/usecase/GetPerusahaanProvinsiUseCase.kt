package com.android.burdacontractor.feature.perusahaan.domain.usecase

import com.android.burdacontractor.core.data.Resource
import kotlinx.coroutines.flow.Flow

interface GetPerusahaanProvinsiUseCase {
    suspend fun execute(): Flow<Resource<List<String>>>
}