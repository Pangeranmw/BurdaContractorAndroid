package com.android.burdacontractor.feature.suratjalan.domain.usecase

import com.android.burdacontractor.core.domain.model.enums.SuratJalanStatus
import com.android.burdacontractor.core.domain.model.enums.SuratJalanTipe
import com.android.burdacontractor.feature.suratjalan.domain.repository.ISuratJalanRepository
import javax.inject.Inject

class GetAllSuratJalanInteractor @Inject constructor(private val suratJalanRepository: ISuratJalanRepository):
    GetAllSuratJalanUseCase
{
    override fun execute(
        tipe: SuratJalanTipe,
        status: SuratJalanStatus,
        date_start: String?,
        date_end: String?,
        size: Int,
        search: String?
    )= suratJalanRepository.getAllSuratJalan(tipe, status, date_start, date_end, size, search)
}