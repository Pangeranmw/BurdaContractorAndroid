package com.android.burdacontractor.feature.suratjalan.domain.usecase

import com.android.burdacontractor.core.data.Resource
import com.android.burdacontractor.feature.suratjalan.data.source.remote.response.SuratJalanDetailItem
import com.android.burdacontractor.feature.suratjalan.domain.model.SuratJalanDetail
import kotlinx.coroutines.flow.Flow

interface GetSuratJalanByIdUseCase {
    suspend fun execute(id: String): Flow<Resource<SuratJalanDetailItem>>
}