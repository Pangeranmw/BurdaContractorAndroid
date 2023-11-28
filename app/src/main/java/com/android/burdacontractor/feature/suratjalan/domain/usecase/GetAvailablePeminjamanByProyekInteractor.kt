package com.android.burdacontractor.feature.suratjalan.domain.usecase

import com.android.burdacontractor.feature.suratjalan.domain.repository.ISuratJalanRepository
import javax.inject.Inject

class GetAvailablePeminjamanByProyekInteractor @Inject constructor(private val suratJalanRepository: ISuratJalanRepository) :
    GetAvailablePeminjamanByProyekUseCase {
    override suspend fun execute(tipe: String, proyekId: String) =
        suratJalanRepository.getAvailablePeminjamanByProyek(tipe, proyekId)
}