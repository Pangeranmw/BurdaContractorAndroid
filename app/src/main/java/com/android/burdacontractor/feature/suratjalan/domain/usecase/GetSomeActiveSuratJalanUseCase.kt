package com.android.burdacontractor.feature.suratjalan.domain.usecase

import com.android.burdacontractor.core.data.Resource
import com.android.burdacontractor.core.domain.model.enums.SuratJalanTipe
import com.android.burdacontractor.feature.suratjalan.data.source.remote.response.DataAllSuratJalanWithCountItem
import kotlinx.coroutines.flow.Flow

interface GetSomeActiveSuratJalanUseCase {
    suspend fun execute(tipe: SuratJalanTipe, size: Int = 5): Flow<Resource<DataAllSuratJalanWithCountItem>>
}