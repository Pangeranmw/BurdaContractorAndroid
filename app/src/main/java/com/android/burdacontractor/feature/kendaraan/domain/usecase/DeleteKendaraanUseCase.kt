package com.android.burdacontractor.feature.kendaraan.domain.usecase

import com.android.burdacontractor.core.data.Resource
import com.android.burdacontractor.core.data.source.remote.response.ErrorMessageResponse
import kotlinx.coroutines.flow.Flow

interface DeleteKendaraanUseCase {
    suspend fun execute(id: String): Flow<Resource<ErrorMessageResponse>>
}