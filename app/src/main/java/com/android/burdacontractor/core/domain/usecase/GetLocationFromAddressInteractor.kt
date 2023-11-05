package com.android.burdacontractor.core.domain.usecase

import com.android.burdacontractor.core.data.GeoCoderRepository
import com.android.burdacontractor.core.data.Resource
import com.android.burdacontractor.core.data.source.remote.response.GetLocationFromAddressResponseItem
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetLocationFromAddressInteractor @Inject constructor(private val geoCoderRepository: GeoCoderRepository) :
    GetLocationFromAddressUseCase {
    override suspend fun execute(q: String): Flow<Resource<List<GetLocationFromAddressResponseItem>>> =
        geoCoderRepository.getLocationFromAddress(q)
}