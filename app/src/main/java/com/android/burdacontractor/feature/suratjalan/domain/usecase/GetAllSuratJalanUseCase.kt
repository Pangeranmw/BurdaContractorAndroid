package com.android.burdacontractor.feature.suratjalan.domain.usecase

import androidx.paging.PagingData
import com.android.burdacontractor.feature.suratjalan.domain.model.AllSuratJalan
import com.android.burdacontractor.core.domain.model.enums.SuratJalanStatus
import com.android.burdacontractor.core.domain.model.enums.SuratJalanTipe
import kotlinx.coroutines.flow.Flow

interface GetAllSuratJalanUseCase {
    suspend fun execute(
        tipe: SuratJalanTipe,
        status: SuratJalanStatus,
        date_start: String? = null,
        date_end: String? = null,
        size: Int = 5,
        search: String? = null,
    ): Flow<PagingData<AllSuratJalan>>
}