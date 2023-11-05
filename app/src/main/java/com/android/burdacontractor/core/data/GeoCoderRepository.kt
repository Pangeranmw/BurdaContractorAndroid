package com.android.burdacontractor.core.data

import com.android.burdacontractor.core.data.source.remote.GeoCoderRemoteDataSource
import com.android.burdacontractor.core.data.source.remote.network.ApiResponse
import com.android.burdacontractor.core.data.source.remote.response.GetLocationFromAddressResponseItem
import com.android.burdacontractor.core.data.source.remote.response.GetLocationFromCoordinateResponse
import com.android.burdacontractor.core.domain.repository.IGeoCoderRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GeoCoderRepository @Inject constructor(
    private val geoCoderRemoteDataSource: GeoCoderRemoteDataSource,
) : IGeoCoderRepository {
    override suspend fun getLocationFromAddress(query: String): Flow<Resource<List<GetLocationFromAddressResponseItem>>> =
        flow {
            emit(Resource.Loading())
            when (val response = geoCoderRemoteDataSource.getLocationFromAddress(
                query
            ).first()) {
                is ApiResponse.Empty -> {}
                is ApiResponse.Success -> {
                    val result = response.data.getLocationFromAddressResponse
                    emit(Resource.Success(result))
                }

                is ApiResponse.Error -> emit(Resource.Error(response.errorMessage))
            }
        }.catch {
            emit(Resource.Error(it.message.toString()))
        }.flowOn(Dispatchers.IO)

    override suspend fun getLocationFromCoordinate(
        latitude: String,
        longitude: String
    ): Flow<Resource<GetLocationFromCoordinateResponse>> =
        flow {
            emit(Resource.Loading())
            when (val response = geoCoderRemoteDataSource.getLocationFromCoordinate(
                latitude, longitude
            ).first()) {
                is ApiResponse.Empty -> {}
                is ApiResponse.Success -> {
                    val result = response.data
                    emit(Resource.Success(result))
                }

                is ApiResponse.Error -> emit(Resource.Error(response.errorMessage))
            }
        }.catch {
            emit(Resource.Error(it.message.toString()))
        }.flowOn(Dispatchers.IO)
}

