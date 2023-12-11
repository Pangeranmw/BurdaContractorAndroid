package com.android.burdacontractor.feature.proyek.domain.usecase

import com.android.burdacontractor.core.data.Resource
import com.android.burdacontractor.feature.proyek.domain.model.LogisticById
import kotlinx.coroutines.flow.Flow

interface GetLogisticByIdUseCase {
    suspend fun execute(id: String): Flow<Resource<LogisticById>>
}