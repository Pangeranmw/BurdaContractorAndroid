package com.android.burdacontractor.feature.gudang.domain.usecase

import com.android.burdacontractor.feature.gudang.domain.repository.IGudangRepository
import javax.inject.Inject

class GetStatistikSuratJalanByGudangInteractor @Inject constructor(private val gudangRepository: IGudangRepository) :
    GetStatistikSuratJalanByGudangUseCase {
    override suspend fun execute(
        id: String,
        tipe: String
    ) = gudangRepository.getStatistikSuratJalanByGudang(id, tipe)
}