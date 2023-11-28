package com.android.burdacontractor.feature.suratjalan.domain.usecase

import com.android.burdacontractor.feature.suratjalan.domain.repository.ISuratJalanRepository
import javax.inject.Inject

class GetAvailableProyekBySuratJalanTypeInteractor @Inject constructor(private val suratJalanRepository: ISuratJalanRepository) :
    GetAvailableProyekBySuratJalanTypeUseCase {
    override suspend fun execute(tipe: String) =
        suratJalanRepository.getAvailableProyekBySuratJalanType(tipe)
}