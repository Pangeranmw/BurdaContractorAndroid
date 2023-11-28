package com.android.burdacontractor.feature.suratjalan.domain.usecase

import com.android.burdacontractor.feature.suratjalan.domain.repository.ISuratJalanRepository
import javax.inject.Inject

class GetAvailablePenggunaanByProyekInteractor @Inject constructor(private val suratJalanRepository: ISuratJalanRepository) :
    GetAvailablePenggunaanByProyekUseCase {
    override suspend fun execute(tipe: String, proyekId: String) =
        suratJalanRepository.getAvailablePenggunaanByProyek(tipe, proyekId)
}