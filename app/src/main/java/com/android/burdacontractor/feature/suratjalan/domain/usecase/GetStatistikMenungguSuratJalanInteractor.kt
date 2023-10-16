package com.android.burdacontractor.feature.suratjalan.domain.usecase

import com.android.burdacontractor.feature.suratjalan.domain.repository.ISuratJalanRepository
import javax.inject.Inject

class GetStatistikMenungguSuratJalanInteractor @Inject constructor(private val suratJalanRepository: ISuratJalanRepository):
    GetStatistikMenungguSuratJalanUseCase
{
    override suspend fun execute()=suratJalanRepository.getStatistikMenungguSuratJalan()
}