package com.android.burdacontractor.core.domain.usecase

import com.android.burdacontractor.core.data.Resource
import kotlinx.coroutines.flow.Flow

interface GetProvinceUseCase {
    suspend fun execute(): Flow<Resource<List<String>>>
}