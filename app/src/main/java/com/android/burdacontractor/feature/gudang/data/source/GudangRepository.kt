package com.android.burdacontractor.feature.gudang.data.source

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.paging.PagingData
import com.android.burdacontractor.core.data.Resource
import com.android.burdacontractor.core.data.source.local.StorageDataSource
import com.android.burdacontractor.core.data.source.remote.network.ApiResponse
import com.android.burdacontractor.feature.gudang.data.source.remote.GudangRemoteDataSource
import com.android.burdacontractor.feature.gudang.domain.model.AllGudang
import com.android.burdacontractor.feature.gudang.domain.model.GudangById
import com.android.burdacontractor.feature.gudang.domain.repository.IGudangRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GudangRepository @Inject constructor(
    private val gudangRemoteDataSource: GudangRemoteDataSource,
    private val storageDataSource: StorageDataSource
) : IGudangRepository {
    override suspend fun getGudangById(id: String): Flow<Resource<GudangById>> = flow {
        emit(Resource.Loading())
        when (val response =
            gudangRemoteDataSource.getGudangById(storageDataSource.getToken(), id)
                .first()) {
            is ApiResponse.Empty -> {}
            is ApiResponse.Success -> {
                val result = response.data.gudang
                emit(Resource.Success(result))
            }

            is ApiResponse.Error -> emit(Resource.Error(response.errorMessage))
        }
    }.catch {
        emit(Resource.Error(it.message.toString()))
    }.flowOn(Dispatchers.IO)

    override suspend fun getGudangProvinsi(): Flow<Resource<List<String>>> = flow {
        emit(Resource.Loading())
        when (val response =
            gudangRemoteDataSource.getGudangProvinsi(storageDataSource.getToken())
                .first()) {
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

    override fun getAllGudang(
        size: Int,
        search: String?,
        filter: String?,
        coordinate: String?,
    ): LiveData<PagingData<AllGudang>> =
        gudangRemoteDataSource.getAllGudang(
            storageDataSource.getToken(), size, search, filter, coordinate
        ).asLiveData()
}

