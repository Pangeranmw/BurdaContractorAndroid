package com.android.burdacontractor.feature.gudang.domain.usecase

import com.android.burdacontractor.feature.gudang.domain.repository.IGudangRepository
import javax.inject.Inject

class GetAllGudangInteractor @Inject constructor(private val gudangRepository: IGudangRepository) :
    GetAllGudangUseCase {
    override fun execute(
        size: Int,
        search: String?,
        filter: String?,
        coordinate: String?,
    ) = gudangRepository.getAllGudang(size, search, filter, coordinate)
}