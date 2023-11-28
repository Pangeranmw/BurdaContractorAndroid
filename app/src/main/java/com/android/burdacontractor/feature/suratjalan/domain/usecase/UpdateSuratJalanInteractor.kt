package com.android.burdacontractor.feature.suratjalan.domain.usecase

import com.android.burdacontractor.feature.suratjalan.data.source.remote.request.AddUpdateSuratJalanBody
import com.android.burdacontractor.feature.suratjalan.domain.repository.ISuratJalanRepository
import javax.inject.Inject

class UpdateSuratJalanInteractor @Inject constructor(private val suratJalanRepository: ISuratJalanRepository) :
    UpdateSuratJalanUseCase {
    override suspend fun execute(
        id: String,
        addUpdateSuratJalanBody: AddUpdateSuratJalanBody
    ) = suratJalanRepository.updateSuratJalan(id, addUpdateSuratJalanBody)
}