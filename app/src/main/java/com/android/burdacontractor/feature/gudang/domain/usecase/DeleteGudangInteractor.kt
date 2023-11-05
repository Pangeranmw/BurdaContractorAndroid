package com.android.burdacontractor.feature.gudang.domain.usecase

import com.android.burdacontractor.feature.gudang.domain.repository.IGudangRepository
import javax.inject.Inject

class DeleteGudangInteractor @Inject constructor(private val gudangRepository: IGudangRepository) :
    DeleteGudangUseCase {
    override suspend fun execute(
        id: String
    ) = gudangRepository.deleteGudang(id)
}