package com.android.burdacontractor.feature.suratjalan.domain.usecase

import com.android.burdacontractor.core.data.Resource
import com.android.burdacontractor.core.data.source.remote.response.ErrorMessageResponse
import kotlinx.coroutines.flow.Flow

interface DeleteSuratJalanChildUseCase {
    suspend fun execute(
        sjChildId: String,
        tipe: String,
    ): Flow<Resource<ErrorMessageResponse>>
}