package com.android.burdacontractor.feature.perusahaan.domain.usecase

import com.android.burdacontractor.core.data.Resource
import com.android.burdacontractor.feature.suratjalan.data.source.remote.response.StatisticCountTitleItem
import kotlinx.coroutines.flow.Flow

interface GetStatistikDeliveryOrderByPerusahaanUseCase {
    suspend fun execute(
        id: String,
    ): Flow<Resource<List<StatisticCountTitleItem>>>
}