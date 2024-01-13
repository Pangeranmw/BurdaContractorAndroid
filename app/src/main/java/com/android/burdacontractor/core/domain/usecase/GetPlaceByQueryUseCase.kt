package com.android.burdacontractor.core.domain.usecase

import com.android.burdacontractor.core.data.Resource
import com.android.burdacontractor.core.data.source.remote.response.PlacesItem
import kotlinx.coroutines.flow.Flow

interface GetPlaceByQueryUseCase {
    suspend fun execute(q: String): Flow<Resource<List<PlacesItem>>>
}