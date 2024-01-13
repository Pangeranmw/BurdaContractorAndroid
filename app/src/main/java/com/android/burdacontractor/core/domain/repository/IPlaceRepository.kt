package com.android.burdacontractor.core.domain.repository

import com.android.burdacontractor.core.data.Resource
import com.android.burdacontractor.core.data.source.remote.response.PlacesItem
import kotlinx.coroutines.flow.Flow

interface IPlaceRepository {
    suspend fun getPlaceByQuery(
        query: String,
    ): Flow<Resource<List<PlacesItem>>>
}