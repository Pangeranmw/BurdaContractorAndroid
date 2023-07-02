package com.android.burdacontractor.feature.suratjalan.domain.usecase

import com.android.burdacontractor.core.domain.model.enums.SuratJalanTipe
import com.android.burdacontractor.feature.suratjalan.domain.repository.ISuratJalanRepository
import javax.inject.Inject

class GetSomeActiveSuratJalanInteractor @Inject constructor(private val suratJalanRepository: ISuratJalanRepository):
    GetSomeActiveSuratJalanUseCase
{
    override suspend fun execute(tipe: SuratJalanTipe, size: Int) = suratJalanRepository.getSomeActiveSuratJalan(tipe, size)
}