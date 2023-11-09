package com.android.burdacontractor.feature.suratjalan.domain.usecase

import com.android.burdacontractor.core.data.Resource
import com.android.burdacontractor.feature.suratjalan.domain.model.SuratJalanDetailItem
import kotlinx.coroutines.flow.Flow

interface GetSuratJalanByIdUseCase {
    suspend fun execute(id: String): Flow<Resource<SuratJalanDetailItem>>
}