package com.android.burdacontractor.feature.suratjalan.domain.usecase

import com.android.burdacontractor.core.data.Resource
import com.android.burdacontractor.core.data.source.remote.response.CountActiveResponse
import kotlinx.coroutines.flow.Flow

interface GetCountActiveSuratJalanUseCase {
    suspend fun execute(): Flow<Resource<CountActiveResponse>>
}