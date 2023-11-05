package com.android.burdacontractor.core.domain.usecase

import com.android.burdacontractor.core.data.DaerahRepository
import com.android.burdacontractor.core.data.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCityByProvinceInteractor @Inject constructor(private val daerahRepository: DaerahRepository) :
    GetCityByProvinceUseCase {
    override suspend fun execute(province: String): Flow<Resource<List<String>>> =
        daerahRepository.getCityByProvince(province)
}