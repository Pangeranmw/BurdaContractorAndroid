package com.android.burdacontractor.feature.suratjalan.domain.usecase

import com.android.burdacontractor.core.domain.model.enums.SuratJalanTipe
import com.android.burdacontractor.feature.suratjalan.domain.repository.ISuratJalanRepository
import javax.inject.Inject

class GetCountActiveSuratJalanInteractor @Inject constructor(private val suratJalanRepository: ISuratJalanRepository):
    GetCountActiveSuratJalanUseCase
{
    override suspend fun execute() = suratJalanRepository.getCountActiveSuratJalan()
}