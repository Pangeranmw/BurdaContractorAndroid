package com.android.burdacontractor.feature.gudang.domain.usecase

import com.android.burdacontractor.core.data.Resource
import kotlinx.coroutines.flow.Flow

interface GetGudangProvinsiUseCase {
    suspend fun execute(): Flow<Resource<List<String>>>
}