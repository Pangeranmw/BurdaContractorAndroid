package com.android.burdacontractor.feature.suratjalan.domain.usecase

import com.android.burdacontractor.feature.suratjalan.domain.repository.ISuratJalanRepository
import javax.inject.Inject

class DeleteSuratJalanInteractor @Inject constructor(private val suratJalanRepository: ISuratJalanRepository):
    DeleteSuratJalanUseCase
{
    override suspend fun execute(suratJalanId: String)=suratJalanRepository.deleteSuratJalan(suratJalanId)
}