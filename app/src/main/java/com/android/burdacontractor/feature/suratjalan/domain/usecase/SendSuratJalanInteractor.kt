package com.android.burdacontractor.feature.suratjalan.domain.usecase

import com.android.burdacontractor.feature.suratjalan.domain.repository.ISuratJalanRepository
import javax.inject.Inject

class SendSuratJalanInteractor @Inject constructor(private val suratJalanRepository: ISuratJalanRepository) :
    SendSuratJalanUseCase {
    override suspend fun execute(id: String) = suratJalanRepository.sendSuratJalan(id)
}