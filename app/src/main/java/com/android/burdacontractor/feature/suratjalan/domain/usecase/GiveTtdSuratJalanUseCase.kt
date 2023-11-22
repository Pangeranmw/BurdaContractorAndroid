package com.android.burdacontractor.feature.suratjalan.domain.usecase

import com.android.burdacontractor.core.data.Resource
import com.android.burdacontractor.core.data.source.remote.response.ErrorMessageResponse
import kotlinx.coroutines.flow.Flow

interface GiveTtdSuratJalanUseCase {
    suspend fun execute(
        id: String,
    ): Flow<Resource<ErrorMessageResponse>>
}