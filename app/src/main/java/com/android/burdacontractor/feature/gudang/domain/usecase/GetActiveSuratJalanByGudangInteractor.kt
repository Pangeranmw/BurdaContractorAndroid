package com.android.burdacontractor.feature.gudang.domain.usecase

import com.android.burdacontractor.feature.gudang.domain.repository.IGudangRepository
import javax.inject.Inject

class GetActiveSuratJalanByGudangInteractor @Inject constructor(private val gudangRepository: IGudangRepository) :
    GetActiveSuratJalanByGudangUseCase {
    override suspend fun execute(
        id: String,
        tipe: String
    ) = gudangRepository.getActiveSuratJalanByGudang(id, tipe)
}