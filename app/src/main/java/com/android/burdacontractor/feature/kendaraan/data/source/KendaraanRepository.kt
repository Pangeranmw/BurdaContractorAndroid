package com.android.burdacontractor.feature.kendaraan.data.source

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.paging.PagingData
import com.android.burdacontractor.core.data.Resource
import com.android.burdacontractor.core.data.source.local.StorageDataSource
import com.android.burdacontractor.core.data.source.remote.network.ApiResponse
import com.android.burdacontractor.feature.gudang.domain.model.GudangById
import com.android.burdacontractor.feature.kendaraan.data.source.remote.KendaraanRemoteDataSource
import com.android.burdacontractor.feature.kendaraan.domain.model.AllKendaraan
import com.android.burdacontractor.feature.kendaraan.domain.model.Kendaraan
import com.android.burdacontractor.feature.kendaraan.domain.model.KendaraanByLogistic
import com.android.burdacontractor.feature.kendaraan.domain.repository.IKendaraanRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class KendaraanRepository @Inject constructor(
    private val kendaraanRemoteDataSource: KendaraanRemoteDataSource,
    private val storageDataSource: StorageDataSource
) : IKendaraanRepository {

    override suspend fun getKendaraanByLogistic(): Flow<Resource<KendaraanByLogistic>> = flow {
        try {
            emit(Resource.Loading())
            when (val response =
                kendaraanRemoteDataSource.getKendaraanByLogistic(storageDataSource.getToken())
                    .first()) {
                is ApiResponse.Empty -> {}
                is ApiResponse.Success -> {
                    if (response.data.kendaraaan == null) {
                        emit(Resource.Error(response.data.message, response.data.kendaraaan))
                    }
                    val result = response.data.kendaraaan!!
                    emit(Resource.Success(result))
                }

                is ApiResponse.Error -> emit(Resource.Error(response.errorMessage))
            }
        } catch (ex: Exception) {
            emit(Resource.Error(ex.message.toString()))
        }
    }

    override suspend fun getKendaraanGudang(): Flow<Resource<List<GudangById>>> = flow {
        try {
            emit(Resource.Loading())
            when (val response =
                kendaraanRemoteDataSource.getKendaraanGudang(storageDataSource.getToken())
                    .first()) {
                is ApiResponse.Empty -> {}
                is ApiResponse.Success -> {
                    val result = response.data.gudang
                    emit(Resource.Success(result))
                }

                is ApiResponse.Error -> emit(Resource.Error(response.errorMessage))
            }
        } catch (ex: Exception) {
            emit(Resource.Error(ex.message.toString()))
        }
    }

    override suspend fun getKendaraanById(id: String): Flow<Resource<Kendaraan>> = flow {
        try {
            emit(Resource.Loading())
            when (val response =
                kendaraanRemoteDataSource.getKendaraanById(storageDataSource.getToken(), id)
                    .first()) {
                is ApiResponse.Empty -> {}
                is ApiResponse.Success -> {
                    val result = response.data.kendaraan
                    emit(Resource.Success(result))
                }

                is ApiResponse.Error -> emit(Resource.Error(response.errorMessage))
            }
        } catch (ex: Exception) {
            emit(Resource.Error(ex.message.toString()))
        }
    }

    override fun getAllKendaraan(
        size: Int,
        filter: String?,
        gudang: String?,
        status: String?,
        search: String?,
    ): LiveData<PagingData<AllKendaraan>> =
        kendaraanRemoteDataSource.getAllKendaraan(
            storageDataSource.getToken(), size, filter, gudang, status, search
        ).asLiveData()
}

