package com.android.burdacontractor.core.domain.repository

import com.android.burdacontractor.core.data.Resource
import kotlinx.coroutines.flow.Flow

interface IDaerahRepository {
    suspend fun getProvince(): Flow<Resource<List<String>>>
    suspend fun getCityByProvince(
        province: String
    ): Flow<Resource<List<String>>>
}