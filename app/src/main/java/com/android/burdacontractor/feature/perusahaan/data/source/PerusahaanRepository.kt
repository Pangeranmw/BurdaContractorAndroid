package com.android.burdacontractor.feature.perusahaan.data.source

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.paging.PagingData
import com.android.burdacontractor.core.data.Resource
import com.android.burdacontractor.core.data.source.local.StorageDataSource
import com.android.burdacontractor.core.data.source.remote.network.ApiResponse
import com.android.burdacontractor.feature.perusahaan.data.source.remote.PerusahaanRemoteDataSource
import com.android.burdacontractor.feature.perusahaan.domain.model.AllPerusahaan
import com.android.burdacontractor.feature.perusahaan.domain.model.PerusahaanById
import com.android.burdacontractor.feature.perusahaan.domain.repository.IPerusahaanRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PerusahaanRepository @Inject constructor(
    private val perusahaanRemoteDataSource: PerusahaanRemoteDataSource,
    private val storageDataSource: StorageDataSource
) : IPerusahaanRepository {
    override suspend fun getPerusahaanById(id: String): Flow<Resource<PerusahaanById>> = flow {
        try {
            emit(Resource.Loading())
            when (val response =
                perusahaanRemoteDataSource.getPerusahaanById(storageDataSource.getToken(), id)
                    .first()) {
                is ApiResponse.Empty -> {}
                is ApiResponse.Success -> {
                    val result = response.data.perusahaan
                    emit(Resource.Success(result))
                }

                is ApiResponse.Error -> emit(Resource.Error(response.errorMessage))
            }
        } catch (ex: Exception) {
            emit(Resource.Error(ex.message.toString()))
        }
    }

    override suspend fun getPerusahaanProvinsi(): Flow<Resource<List<String>>> = flow {
        try {
            emit(Resource.Loading())
            when (val response =
                perusahaanRemoteDataSource.getPerusahaanProvinsi(storageDataSource.getToken())
                    .first()) {
                is ApiResponse.Empty -> {}
                is ApiResponse.Success -> {
                    val result = response.data.provinsi
                    emit(Resource.Success(result))
                }

                is ApiResponse.Error -> emit(Resource.Error(response.errorMessage))
            }
        } catch (ex: Exception) {
            emit(Resource.Error(ex.message.toString()))
        }
    }

    override fun getAllPerusahaan(
        size: Int,
        search: String?,
        filter: String?,
        coordinate: String?,
    ): LiveData<PagingData<AllPerusahaan>> =
        perusahaanRemoteDataSource.getAllPerusahaan(
            storageDataSource.getToken(), size, search, filter, coordinate
        ).asLiveData()
}

