package com.android.burdacontractor.core.domain.usecase

import com.android.burdacontractor.core.data.GeoCoderRepository
import com.android.burdacontractor.core.data.Resource
import com.android.burdacontractor.core.data.source.remote.response.GetLocationFromCoordinateResponse
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetLocationFromCoordinateInteractor @Inject constructor(private val geoCoderRepository: GeoCoderRepository) :
    GetLocationFromCoordinateUseCase {
    override suspend fun execute(
        lat: String,
        lon: String
    ): Flow<Resource<GetLocationFromCoordinateResponse>> =
        geoCoderRepository.getLocationFromCoordinate(lat, lon)
}