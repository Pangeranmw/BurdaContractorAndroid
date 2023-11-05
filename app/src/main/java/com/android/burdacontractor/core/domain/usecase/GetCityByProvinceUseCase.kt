package com.android.burdacontractor.core.domain.usecase

import com.android.burdacontractor.core.data.Resource
import kotlinx.coroutines.flow.Flow

interface GetCityByProvinceUseCase {
    suspend fun execute(province: String): Flow<Resource<List<String>>>
}