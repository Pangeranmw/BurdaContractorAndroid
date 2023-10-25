package com.android.burdacontractor.feature.kendaraan.domain.usecase

import com.android.burdacontractor.core.data.Resource
import com.android.burdacontractor.feature.gudang.domain.model.GudangById
import kotlinx.coroutines.flow.Flow

interface GetKendaraanGudangUseCase {
    suspend fun execute(): Flow<Resource<List<GudangById>>>
}