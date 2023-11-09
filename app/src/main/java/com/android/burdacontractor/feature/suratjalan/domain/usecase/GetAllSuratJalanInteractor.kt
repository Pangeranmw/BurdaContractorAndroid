package com.android.burdacontractor.feature.suratjalan.domain.usecase

import com.android.burdacontractor.core.domain.model.enums.CreatedByOrFor
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
        dateStart: String?,
        dateEnd: String?,
        size: Int,
        search: String?,
        createdByOrFor: CreatedByOrFor,
    ) = suratJalanRepository.getAllSuratJalan(
        tipe,
        status,
        dateStart,
        dateEnd,
        size,
        search,
        createdByOrFor
    )
}