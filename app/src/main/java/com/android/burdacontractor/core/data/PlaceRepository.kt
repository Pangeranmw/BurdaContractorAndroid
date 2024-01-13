package com.android.burdacontractor.core.data

import com.android.burdacontractor.core.data.source.remote.PlaceRemoteDataSource
import com.android.burdacontractor.core.data.source.remote.network.ApiResponse
import com.android.burdacontractor.core.data.source.remote.response.PlacesItem
import com.android.burdacontractor.core.domain.repository.IPlaceRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PlaceRepository @Inject constructor(
    private val placeRemoteDataSource: PlaceRemoteDataSource,
) : IPlaceRepository {
    override suspend fun getPlaceByQuery(query: String): Flow<Resource<List<PlacesItem>>> =
        flow {
            emit(Resource.Loading())
            when (val response = placeRemoteDataSource.getPlaceByQuery(
                query
            ).first()) {
                is ApiResponse.Empty -> {}
                is ApiResponse.Success -> {
                    val result = response.data.places
                    emit(Resource.Success(result))
                }

                is ApiResponse.Error -> emit(Resource.Error(response.errorMessage))
            }
        }.catch {
            emit(Resource.Error(it.message.toString()))
        }.flowOn(Dispatchers.IO)
}

