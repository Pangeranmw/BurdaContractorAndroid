package com.android.burdacontractor.core.domain.usecase

import com.android.burdacontractor.core.data.Resource
import com.android.burdacontractor.core.data.source.remote.response.GetLocationFromAddressResponseItem
import com.android.burdacontractor.core.domain.repository.IGeoCoderRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetLocationFromAddressInteractor @Inject constructor(private val geoCoderRepository: IGeoCoderRepository) :
    GetLocationFromAddressUseCase {
    override suspend fun execute(q: String): Flow<Resource<List<GetLocationFromAddressResponseItem>>> =
        geoCoderRepository.getLocationFromAddress(q)
}