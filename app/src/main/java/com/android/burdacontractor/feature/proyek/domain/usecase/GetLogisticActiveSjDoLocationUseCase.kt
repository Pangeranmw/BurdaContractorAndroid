package com.android.burdacontractor.feature.proyek.domain.usecase

import com.android.burdacontractor.core.data.Resource
import com.android.burdacontractor.feature.proyek.domain.model.ActiveSjDoLocation
import kotlinx.coroutines.flow.Flow

interface GetLogisticActiveSjDoLocationUseCase {
    suspend fun execute(id: String): Flow<Resource<List<ActiveSjDoLocation>>>
}