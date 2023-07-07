package com.android.burdacontractor.feature.suratjalan.domain.usecase

import com.android.burdacontractor.feature.suratjalan.domain.repository.ISuratJalanRepository
import javax.inject.Inject

class GetAllSuratJalanDalamPerjalananByUserInteractor @Inject constructor(private val suratJalanRepository: ISuratJalanRepository):
    GetAllSuratJalanDalamPerjalananByUserUseCase
{
    override suspend fun execute()=suratJalanRepository.getAllSuratJalanDalamPerjalananByUser()
}