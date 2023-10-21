package com.android.burdacontractor.feature.perusahaan.domain.usecase

import com.android.burdacontractor.core.data.Resource
import com.android.burdacontractor.feature.perusahaan.domain.model.PerusahaanById
import kotlinx.coroutines.flow.Flow

interface GetPerusahaanByIdUseCase {
    suspend fun execute(id: String): Flow<Resource<PerusahaanById>>
}