package com.android.burdacontractor.feature.logistic.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.paging.PagingData
import com.android.burdacontractor.core.data.Resource
import com.android.burdacontractor.core.data.source.local.StorageDataSource
import com.android.burdacontractor.core.data.source.remote.network.ApiResponse
import com.android.burdacontractor.feature.logistic.data.source.remote.LogisticRemoteDataSource
import com.android.burdacontractor.feature.logistic.domain.model.ActiveSjDoLocation
import com.android.burdacontractor.feature.logistic.domain.model.AllLogistic
import com.android.burdacontractor.feature.logistic.domain.model.LogisticById
import com.android.burdacontractor.feature.logistic.domain.repository.ILogisticRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LogisticRepository @Inject constructor(
    private val logisticRemoteDataSource: LogisticRemoteDataSource,
    private val storageDataSource: StorageDataSource
) : ILogisticRepository {
    override fun getAllLogistic(
        search: String?,
        coordinate: String?,
        size: Int
    ): LiveData<PagingData<AllLogistic>> =
        logisticRemoteDataSource.getAllLogistic(
            storageDataSource.getToken(), search, coordinate, size
        ).asLiveData()

    override suspend fun getAllLogisticNoPaging(): Flow<Resource<List<AllLogistic>>> =
        flow {
            emit(Resource.Loading())
            when (val response =
                logisticRemoteDataSource.getAllLogisticNoPaging(
                    storageDataSource.getToken()
                )
                    .first()) {
                is ApiResponse.Empty -> {}
                is ApiResponse.Success -> {
                    val result = response.data.logistic
                    emit(Resource.Success(result, response.data.message))
                }

                is ApiResponse.Error -> emit(Resource.Error(response.errorMessage))
            }
        }.catch {
            emit(Resource.Error(it.message.toString()))
        }.flowOn(Dispatchers.IO)

    override suspend fun getLogisticById(id: String): Flow<Resource<LogisticById>> =
        flow {
            emit(Resource.Loading())
            when (val response =
                logisticRemoteDataSource.getLogisticById(storageDataSource.getToken(), id)
                    .first()) {
                is ApiResponse.Empty -> {}
                is ApiResponse.Success -> {
                    val result = response.data.logistic
                    emit(Resource.Success(result, response.data.message))
                }

                is ApiResponse.Error -> emit(Resource.Error(response.errorMessage))
            }
        }.catch {
            emit(Resource.Error(it.message.toString()))
        }.flowOn(Dispatchers.IO)

    override suspend fun getLogisticActiveSjDoLocation(id: String): Flow<Resource<List<ActiveSjDoLocation>>> =
        flow {
            emit(Resource.Loading())
            when (val response =
                logisticRemoteDataSource.getLogisticActiveSjDoLocation(
                    storageDataSource.getToken(),
                    id
                )
                    .first()) {
                is ApiResponse.Empty -> {}
                is ApiResponse.Success -> {
                    val result = response.data.activeLocation
                    emit(Resource.Success(result, response.data.message))
                }

                is ApiResponse.Error -> emit(Resource.Error(response.errorMessage))
            }
        }.catch {
            emit(Resource.Error(it.message.toString()))
        }.flowOn(Dispatchers.IO)
}

