package com.android.burdacontractor.feature.gudang.domain.usecase

import com.android.burdacontractor.feature.gudang.domain.repository.IGudangRepository
import javax.inject.Inject

class GetGudangProvinsiInteractor @Inject constructor(private val gudangRepository: IGudangRepository) :
    GetGudangProvinsiUseCase {
    override suspend fun execute() = gudangRepository.getGudangProvinsi()
}