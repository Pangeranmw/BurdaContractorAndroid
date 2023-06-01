package com.android.burdacontractor.feature.suratjalan.domain.usecase

import com.android.burdacontractor.core.data.Resource
import com.android.burdacontractor.core.data.source.remote.response.ErrorMessageResponse
import kotlinx.coroutines.flow.Flow

interface DeleteSuratJalanUseCase {
    suspend fun execute(
        suratJalanId: String,
    ): Flow<Resource<ErrorMessageResponse>>
}