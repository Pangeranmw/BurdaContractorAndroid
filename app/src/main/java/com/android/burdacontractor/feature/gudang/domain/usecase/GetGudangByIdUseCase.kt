package com.android.burdacontractor.feature.gudang.domain.usecase

import com.android.burdacontractor.core.data.Resource
import com.android.burdacontractor.feature.gudang.domain.model.GudangById
import kotlinx.coroutines.flow.Flow

interface GetGudangByIdUseCase {
    suspend fun execute(id: String): Flow<Resource<GudangById>>
}