package com.android.burdacontractor.feature.suratjalan.domain.usecase

import com.android.burdacontractor.feature.suratjalan.domain.repository.ISuratJalanRepository
import javax.inject.Inject

class MarkCompleteSuratJalanInteractor @Inject constructor(private val suratJalanRepository: ISuratJalanRepository) :
    MarkCompleteSuratJalanUseCase {
    override suspend fun execute(id: String) = suratJalanRepository.markCompleteSuratJalan(id)
}