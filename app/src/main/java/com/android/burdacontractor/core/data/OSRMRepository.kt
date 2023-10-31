package com.android.burdacontractor.core.data

import com.android.burdacontractor.core.data.source.remote.OSRMRemoteDataSource
import com.android.burdacontractor.core.data.source.remote.network.ApiResponse
import com.android.burdacontractor.core.data.source.remote.response.DistanceMatrixResponse
import com.android.burdacontractor.core.domain.repository.IOSRMRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OSRMRepository @Inject constructor(
    private val osrmDataSource: OSRMRemoteDataSource,
) : IOSRMRepository {
    override suspend fun getDistanceMatrix(coordinates: String): Flow<Resource<DistanceMatrixResponse>> =
        flow {
            emit(Resource.Loading())
            when (val response = osrmDataSource.getDistanceMatrix(
                coordinates
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

