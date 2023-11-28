package com.android.burdacontractor.feature.suratjalan.domain.usecase

import com.android.burdacontractor.feature.suratjalan.data.source.remote.request.AddUpdateSuratJalanBody
import com.android.burdacontractor.feature.suratjalan.domain.repository.ISuratJalanRepository
import javax.inject.Inject

class AddSuratJalanInteractor @Inject constructor(private val suratJalanRepository: ISuratJalanRepository) :
    AddSuratJalanUseCase {
    override suspend fun execute(
        addUpdateSuratJalanBody: AddUpdateSuratJalanBody
    ) = suratJalanRepository.addSuratJalan(addUpdateSuratJalanBody)
}