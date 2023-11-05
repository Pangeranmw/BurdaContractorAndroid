package com.android.burdacontractor.core.domain.usecase

import com.android.burdacontractor.core.data.DaerahRepository
import com.android.burdacontractor.core.data.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetProvinceInteractor @Inject constructor(private val daerahRepository: DaerahRepository) :
    GetProvinceUseCase {
    override suspend fun execute(): Flow<Resource<List<String>>> = daerahRepository.getProvince()
}