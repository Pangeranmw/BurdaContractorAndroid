package com.android.burdacontractor.feature.suratjalan.domain.usecase

import com.android.burdacontractor.feature.suratjalan.domain.repository.ISuratJalanRepository
import javax.inject.Inject

class GiveTtdSuratJalanInteractor @Inject constructor(private val suratJalanRepository: ISuratJalanRepository) :
    GiveTtdSuratJalanUseCase {
    override suspend fun execute(id: String) = suratJalanRepository.giveTtdSuratJalan(id)
}