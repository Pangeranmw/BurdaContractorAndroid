package com.android.burdacontractor.feature.suratjalan.domain.usecase

import androidx.paging.PagingData
import com.android.burdacontractor.core.domain.model.enums.SuratJalanStatus
import com.android.burdacontractor.core.domain.model.enums.SuratJalanTipe
import com.android.burdacontractor.feature.suratjalan.data.source.remote.response.SuratJalanItem
import com.android.burdacontractor.feature.suratjalan.domain.model.AllSuratJalan
import kotlinx.coroutines.flow.Flow

interface GetAllSuratJalanUseCase {
    fun execute(
        tipe: SuratJalanTipe,
        status: SuratJalanStatus,
        date_start: String? = null,
        date_end: String? = null,
        size: Int = 5,
        search: String? = null,
    ): Flow<PagingData<SuratJalanItem>>
}