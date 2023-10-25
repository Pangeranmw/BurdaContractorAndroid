package com.android.burdacontractor.feature.kendaraan.domain.usecase

import com.android.burdacontractor.core.data.Resource
import com.android.burdacontractor.feature.kendaraan.domain.model.Kendaraan
import kotlinx.coroutines.flow.Flow

interface GetKendaraanByIdUseCase {
    suspend fun execute(id: String): Flow<Resource<Kendaraan>>
}