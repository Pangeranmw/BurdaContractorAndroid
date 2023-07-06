package com.android.burdacontractor.feature.kendaraan.domain.repository

import com.android.burdacontractor.core.data.Resource
import com.android.burdacontractor.feature.kendaraan.domain.model.KendaraanByLogistic
import kotlinx.coroutines.flow.Flow

interface IKendaraanRepository {

    suspend fun getKendaraanByLogistic(): Flow<Resource<KendaraanByLogistic>>
}