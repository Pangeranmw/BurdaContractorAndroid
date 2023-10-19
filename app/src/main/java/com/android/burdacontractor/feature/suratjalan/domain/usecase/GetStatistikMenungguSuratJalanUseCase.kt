package com.android.burdacontractor.feature.suratjalan.domain.usecase

import com.android.burdacontractor.core.data.Resource
import com.android.burdacontractor.feature.suratjalan.data.source.remote.response.StatisticCountTitleItem
import kotlinx.coroutines.flow.Flow

interface GetStatistikMenungguSuratJalanUseCase {
    suspend fun execute(): Flow<Resource<List<StatisticCountTitleItem>>>
}