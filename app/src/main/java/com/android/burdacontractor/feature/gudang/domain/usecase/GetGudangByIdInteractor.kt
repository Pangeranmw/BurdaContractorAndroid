package com.android.burdacontractor.feature.gudang.domain.usecase

import com.android.burdacontractor.feature.gudang.domain.repository.IGudangRepository
import javax.inject.Inject

class GetGudangByIdInteractor @Inject constructor(private val gudangRepository: IGudangRepository) :
    GetGudangByIdUseCase {
    override suspend fun execute(id: String) = gudangRepository.getGudangById(id)
}