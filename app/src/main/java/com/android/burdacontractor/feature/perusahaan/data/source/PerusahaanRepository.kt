package com.android.burdacontractor.feature.perusahaan.data.source

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.paging.PagingData
import com.android.burdacontractor.core.data.Resource
import com.android.burdacontractor.core.data.source.local.StorageDataSource
import com.android.burdacontractor.core.data.source.remote.network.ApiResponse
import com.android.burdacontractor.core.data.source.remote.response.ErrorMessageResponse
import com.android.burdacontractor.feature.deliveryorder.domain.model.AllDeliveryOrder
import com.android.burdacontractor.feature.perusahaan.data.source.remote.PerusahaanRemoteDataSource
import com.android.burdacontractor.feature.perusahaan.domain.model.AllPerusahaan
import com.android.burdacontractor.feature.perusahaan.domain.model.PerusahaanById
import com.android.burdacontractor.feature.perusahaan.domain.repository.IPerusahaanRepository
import com.android.burdacontractor.feature.suratjalan.data.source.remote.response.StatisticCountTitleItem
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
class PerusahaanRepository @Inject constructor(
    private val perusahaanRemoteDataSource: PerusahaanRemoteDataSource,
    private val storageDataSource: StorageDataSource
) : IPerusahaanRepository {
    override suspend fun getPerusahaanById(id: String): Flow<Resource<PerusahaanById>> =
        flow {
            emit(Resource.Loading())
            when (val response =
                perusahaanRemoteDataSource.getPerusahaanById(storageDataSource.getToken(), id)
                    .first()) {
                is ApiResponse.Empty -> {}
                is ApiResponse.Success -> {
                    val result = response.data.perusahaan
                    emit(Resource.Success(result, response.data.message))
                }

                is ApiResponse.Error -> emit(Resource.Error(response.errorMessage))
            }
        }.catch {
            emit(Resource.Error(it.message.toString()))
        }.flowOn(Dispatchers.IO)

    override suspend fun getPerusahaanProvinsi(): Flow<Resource<List<String>>> =
        flow {
            emit(Resource.Loading())
            when (val response =
                perusahaanRemoteDataSource.getPerusahaanProvinsi(storageDataSource.getToken())
                    .first()) {
                is ApiResponse.Empty -> {}
                is ApiResponse.Success -> {
                    val result = response.data.provinsi
                    emit(Resource.Success(result, response.data.message))
                }

                is ApiResponse.Error -> emit(Resource.Error(response.errorMessage))
            }
        }.catch {
            emit(Resource.Error(it.message.toString()))
        }.flowOn(Dispatchers.IO)

    override suspend fun getActiveDeliveryOrderByPerusahaan(id: String): Flow<Resource<List<AllDeliveryOrder>>> =
        flow {
            emit(Resource.Loading())
            when (val response =
                perusahaanRemoteDataSource.getActiveDeliveryOrderByPerusahaan(
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

    override suspend fun getStatistikDeliveryOrderByPerusahaan(id: String): Flow<Resource<List<StatisticCountTitleItem>>> =
        flow {
            emit(Resource.Loading())
            when (val response =
                perusahaanRemoteDataSource.getStatistikDeliveryOrderByPerusahaan(
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

    override suspend fun updatePerusahaan(
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
                perusahaanRemoteDataSource.updatePerusahaan(
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

    override suspend fun deletePerusahaan(
        id: String,
    ): Flow<Resource<ErrorMessageResponse>> =
        flow {
            emit(Resource.Loading())
            when (val response =
                perusahaanRemoteDataSource.deletePerusahaan(storageDataSource.getToken(), id)
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

    override suspend fun addPerusahaan(
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
                perusahaanRemoteDataSource.addPerusahaan(
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

