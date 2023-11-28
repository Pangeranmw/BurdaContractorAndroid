package com.android.burdacontractor.feature.suratjalan.domain.usecase

import com.android.burdacontractor.core.data.Resource
import com.android.burdacontractor.feature.suratjalan.data.source.remote.request.AddUpdateSuratJalanBody
import kotlinx.coroutines.flow.Flow

interface AddSuratJalanUseCase {
    suspend fun execute(
        addUpdateSuratJalanBody: AddUpdateSuratJalanBody
    ): Flow<Resource<String>>
}