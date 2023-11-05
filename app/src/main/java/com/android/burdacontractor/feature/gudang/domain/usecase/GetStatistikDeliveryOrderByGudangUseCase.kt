package com.android.burdacontractor.feature.gudang.domain.usecase

import com.android.burdacontractor.core.data.Resource
import com.android.burdacontractor.feature.suratjalan.data.source.remote.response.StatisticCountTitleItem
import kotlinx.coroutines.flow.Flow

interface GetStatistikDeliveryOrderByGudangUseCase {
    suspend fun execute(
        id: String,
    ): Flow<Resource<List<StatisticCountTitleItem>>>
}