package com.android.burdacontractor.core.domain.usecase

import com.android.burdacontractor.core.data.PlaceRepository
import com.android.burdacontractor.core.data.Resource
import com.android.burdacontractor.core.data.source.remote.response.PlacesItem
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPlaceByQueryInteractor @Inject constructor(private val placeRepo: PlaceRepository) :
    GetPlaceByQueryUseCase {
    override suspend fun execute(q: String): Flow<Resource<List<PlacesItem>>> =
        placeRepo.getPlaceByQuery(q)
}