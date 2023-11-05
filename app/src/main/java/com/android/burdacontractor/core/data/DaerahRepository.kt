package com.android.burdacontractor.core.data

import com.android.burdacontractor.core.data.source.remote.DaerahRemoteDataSource
import com.android.burdacontractor.core.data.source.remote.network.ApiResponse
import com.android.burdacontractor.core.domain.repository.IDaerahRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DaerahRepository @Inject constructor(
    private val daerahDataSource: DaerahRemoteDataSource,
) : IDaerahRepository {
    override suspend fun getCityByProvince(province: String): Flow<Resource<List<String>>> =
        flow {
            emit(Resource.Loading())
            when (val response = daerahDataSource.getCityByProvince(
                province
            ).first()) {
                is ApiResponse.Empty -> {}
                is ApiResponse.Success -> {
                    val result = response.data.kota
                    emit(Resource.Success(result))
                }

                is ApiResponse.Error -> emit(Resource.Error(response.errorMessage))
            }
        }.catch {
            emit(Resource.Error(it.message.toString()))
        }.flowOn(Dispatchers.IO)

    override suspend fun getProvince(): Flow<Resource<List<String>>> =
        flow {
            emit(Resource.Loading())
            when (val response = daerahDataSource.getProvince().first()) {
                is ApiResponse.Empty -> {}
                is ApiResponse.Success -> {
                    val result = response.data.provinsi
                    emit(Resource.Success(result))
                }

                is ApiResponse.Error -> emit(Resource.Error(response.errorMessage))
            }
        }.catch {
            emit(Resource.Error(it.message.toString()))
        }.flowOn(Dispatchers.IO)
}

