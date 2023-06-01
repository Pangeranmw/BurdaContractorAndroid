package com.android.burdacontractor.feature.suratjalan.domain.usecase

import com.android.burdacontractor.feature.suratjalan.domain.repository.ISuratJalanRepository
import javax.inject.Inject

class GetSuratJalanByIdInteractor @Inject constructor(private val suratJalanRepository: ISuratJalanRepository):
    GetSuratJalanByIdUseCase
{
    override suspend fun execute(id: String) = suratJalanRepository.getSuratJalanById(id)
}