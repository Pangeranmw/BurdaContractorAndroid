package com.android.burdacontractor.feature.gudang.domain.usecase

import com.android.burdacontractor.core.data.Resource
import com.android.burdacontractor.feature.suratjalan.data.source.remote.response.StatisticCountTitleItem
import kotlinx.coroutines.flow.Flow

interface GetStatistikSuratJalanByGudangUseCase {
    suspend fun execute(
        id: String,
        tipe: String,
    ): Flow<Resource<List<StatisticCountTitleItem>>>
}