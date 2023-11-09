package com.android.burdacontractor.feature.suratjalan.domain.usecase

import androidx.paging.PagingData
import com.android.burdacontractor.core.domain.model.enums.CreatedByOrFor
import com.android.burdacontractor.core.domain.model.enums.SuratJalanStatus
import com.android.burdacontractor.core.domain.model.enums.SuratJalanTipe
import com.android.burdacontractor.feature.suratjalan.domain.model.AllSuratJalan
import kotlinx.coroutines.flow.Flow

interface GetAllSuratJalanUseCase {
    fun execute(
        tipe: SuratJalanTipe,
        status: SuratJalanStatus,
        dateStart: String? = null,
        dateEnd: String? = null,
        size: Int = 5,
        search: String? = null,
        createdByOrFor: CreatedByOrFor,
    ): Flow<PagingData<AllSuratJalan>>
}