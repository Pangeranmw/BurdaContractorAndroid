package com.android.burdacontractor.feature.kendaraan.domain.usecase

import com.android.burdacontractor.core.data.Resource
import com.android.burdacontractor.feature.kendaraan.data.source.remote.response.KendaraanByLogisticItem
import com.android.burdacontractor.feature.kendaraan.domain.model.KendaraanByLogistic
import kotlinx.coroutines.flow.Flow

interface GetKendaraanByLogisticUseCase {
    suspend fun execute(): Flow<Resource<KendaraanByLogisticItem>>
}