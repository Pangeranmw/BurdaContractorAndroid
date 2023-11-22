package com.android.burdacontractor.feature.suratjalan.domain.usecase

import com.android.burdacontractor.feature.suratjalan.domain.repository.ISuratJalanRepository
import javax.inject.Inject

class DeleteSuratJalanChildInteractor @Inject constructor(private val suratJalanRepository: ISuratJalanRepository) :
    DeleteSuratJalanChildUseCase {
    override suspend fun execute(sjChildId: String, tipe: String) =
        suratJalanRepository.deleteSuratJalanChild(sjChildId, tipe)
}