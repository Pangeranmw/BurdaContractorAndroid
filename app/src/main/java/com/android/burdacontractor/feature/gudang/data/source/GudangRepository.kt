package com.android.burdacontractor.feature.gudang.data.source

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.paging.PagingData
import com.android.burdacontractor.core.data.Resource
import com.android.burdacontractor.core.data.source.local.StorageDataSource
import com.android.burdacontractor.core.data.source.remote.network.ApiResponse
import com.android.burdacontractor.core.data.source.remote.response.ErrorMessageResponse
import com.android.burdacontractor.feature.deliveryorder.domain.model.AllDeliveryOrder
import com.android.burdacontractor.feature.gudang.data.source.remote.GudangRemoteDataSource
import com.android.burdacontractor.feature.gudang.domain.model.AllGudang
import com.android.burdacontractor.feature.gudang.domain.model.GudangById
import com.android.burdacontractor.feature.gudang.domain.repository.IGudangRepository
import com.android.burdacontractor.feature.suratjalan.data.source.remote.response.StatisticCountTitleItem
import com.android.burdacontractor.feature.suratjalan.domain.model.AllSuratJalan
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.io.File
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

    override suspend fun getActiveDeliveryOrderByGudang(id: String): Flow<Resource<List<AllDeliveryOrder>>> =
        flow {
            emit(Resource.Loading())
            when (val response =
                gudangRemoteDataSource.getActiveDeliveryOrderByGudang(
                    storageDataSource.getToken(),
                    id
                )
                    .first()) {
                is ApiResponse.Empty -> {}
                is ApiResponse.Success -> {
                    val result = response.data.deliveryOrder
                    emit(Resource.Success(result, response.data.message))
                }

                is ApiResponse.Error -> emit(Resource.Error(response.errorMessage))
            }
        }.catch {
            emit(Resource.Error(it.message.toString()))
        }.flowOn(Dispatchers.IO)

    override suspend fun getStatistikDeliveryOrderByGudang(id: String): Flow<Resource<List<StatisticCountTitleItem>>> =
        flow {
            emit(Resource.Loading())
            when (val response =
                gudangRemoteDataSource.getStatistikDeliveryOrderByGudang(
                    storageDataSource.getToken(),
                    id
                )
                    .first()) {
                is ApiResponse.Empty -> {}
                is ApiResponse.Success -> {
                    val result = response.data.statisticCountTitleItems
                    emit(Resource.Success(result, response.data.message))
                }

                is ApiResponse.Error -> emit(Resource.Error(response.errorMessage))
            }
        }.catch {
            emit(Resource.Error(it.message.toString()))
        }.flowOn(Dispatchers.IO)

    override suspend fun getActiveSuratJalanByGudang(
        id: String,
        tipe: String
    ): Flow<Resource<List<AllSuratJalan>>> =
        flow {
            emit(Resource.Loading())
            when (val response =
                gudangRemoteDataSource.getActiveSuratJalanByGudang(
                    storageDataSource.getToken(),
                    id,
                    tipe
                )
                    .first()) {
                is ApiResponse.Empty -> {}
                is ApiResponse.Success -> {
                    val result = response.data.suratJalan
                    emit(Resource.Success(result, response.data.message))
                }

                is ApiResponse.Error -> emit(Resource.Error(response.errorMessage))
            }
        }.catch {
            emit(Resource.Error(it.message.toString()))
        }.flowOn(Dispatchers.IO)

    override suspend fun getStatistikSuratJalanByGudang(
        id: String,
        tipe: String
    ): Flow<Resource<List<StatisticCountTitleItem>>> =
        flow {
            emit(Resource.Loading())
            when (val response =
                gudangRemoteDataSource.getStatistikSuratJalanByGudang(
                    storageDataSource.getToken(),
                    id,
                    tipe
                )
                    .first()) {
                is ApiResponse.Empty -> {}
                is ApiResponse.Success -> {
                    val result = response.data.statisticCountTitleItems
                    emit(Resource.Success(result, response.data.message))
                }

                is ApiResponse.Error -> emit(Resource.Error(response.errorMessage))
            }
        }.catch {
            emit(Resource.Error(it.message.toString()))
        }.flowOn(Dispatchers.IO)

    override suspend fun updateGudang(
        id: String,
        nama: String,
        alamat: String,
        provinsi: String,
        kota: String,
        latitude: String,
        longitude: String,
        gambar: File?,
    ): Flow<Resource<String>> =
        flow {
            emit(Resource.Loading())
            when (val response =
                gudangRemoteDataSource.updateGudang(
                    storageDataSource.getToken(),
                    id,
                    nama,
                    alamat,
                    provinsi,
                    kota,
                    latitude,
                    longitude,
                    gambar
                )
                    .first()) {
                is ApiResponse.Empty -> {}
                is ApiResponse.Success -> {
                    val result = response.data.id
                    emit(Resource.Success(result, response.data.message))
                }

                is ApiResponse.Error -> emit(Resource.Error(response.errorMessage))
            }
        }.catch {
            emit(Resource.Error(it.message.toString()))
        }.flowOn(Dispatchers.IO)

    override suspend fun deleteGudang(
        id: String,
    ): Flow<Resource<ErrorMessageResponse>> =
        flow {
            emit(Resource.Loading())
            when (val response =
                gudangRemoteDataSource.deleteGudang(storageDataSource.getToken(), id)
                    .first()) {
                is ApiResponse.Empty -> {}
                is ApiResponse.Success -> {
                    val result = response.data
                    emit(Resource.Success(result, response.data.message))
                }

                is ApiResponse.Error -> emit(Resource.Error(response.errorMessage))
            }
        }.catch {
            emit(Resource.Error(it.message.toString()))
        }.flowOn(Dispatchers.IO)

    override suspend fun addGudang(
        nama: String,
        alamat: String,
        provinsi: String,
        kota: String,
        latitude: String,
        longitude: String,
        gambar: File,
    ): Flow<Resource<String>> =
        flow {
            emit(Resource.Loading())
            when (val response =
                gudangRemoteDataSource.addGudang(
                    storageDataSource.getToken(),
                    nama,
                    alamat,
                    provinsi,
                    kota,
                    latitude,
                    longitude,
                    gambar
                )
                    .first()) {
                is ApiResponse.Empty -> {}
                is ApiResponse.Success -> {
                    val result = response.data.id
                    emit(Resource.Success(result, response.data.message))
                }

                is ApiResponse.Error -> emit(Resource.Error(response.errorMessage))
            }
        }.catch {
            emit(Resource.Error(it.message.toString()))
        }.flowOn(Dispatchers.IO)

}

