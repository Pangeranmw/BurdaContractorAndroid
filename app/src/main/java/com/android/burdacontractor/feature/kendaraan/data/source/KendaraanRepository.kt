package com.android.burdacontractor.feature.kendaraan.data.source

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.paging.PagingData
import com.android.burdacontractor.core.data.Resource
import com.android.burdacontractor.core.data.source.local.StorageDataSource
import com.android.burdacontractor.core.data.source.remote.network.ApiResponse
import com.android.burdacontractor.core.data.source.remote.response.ErrorMessageResponse
import com.android.burdacontractor.core.domain.model.enums.SuratJalanTipe
import com.android.burdacontractor.feature.deliveryorder.domain.model.AllDeliveryOrder
import com.android.burdacontractor.feature.gudang.domain.model.GudangById
import com.android.burdacontractor.feature.kendaraan.data.source.remote.KendaraanRemoteDataSource
import com.android.burdacontractor.feature.kendaraan.domain.model.AllKendaraan
import com.android.burdacontractor.feature.kendaraan.domain.model.Kendaraan
import com.android.burdacontractor.feature.kendaraan.domain.model.KendaraanByLogistic
import com.android.burdacontractor.feature.kendaraan.domain.repository.IKendaraanRepository
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
class KendaraanRepository @Inject constructor(
    private val kendaraanRemoteDataSource: KendaraanRemoteDataSource,
    private val storageDataSource: StorageDataSource
) : IKendaraanRepository {

    override suspend fun getKendaraanByLogistic(): Flow<Resource<KendaraanByLogistic>> = flow {
        emit(Resource.Loading())
        when (val response =
            kendaraanRemoteDataSource.getKendaraanByLogistic(storageDataSource.getToken())
                .first()) {
            is ApiResponse.Empty -> {}
            is ApiResponse.Success -> {
                val result = response.data.kendaraaan
                emit(Resource.Success(result, response.data.message))
            }
            is ApiResponse.Error -> emit(Resource.Error(response.errorMessage))
        }
    }.catch {
        emit(Resource.Error(it.message.toString()))
    }.flowOn(Dispatchers.IO)


    override suspend fun getKendaraanGudang(): Flow<Resource<List<GudangById>>> =
        flow {
            emit(Resource.Loading())
            when (val response =
                kendaraanRemoteDataSource.getKendaraanGudang(storageDataSource.getToken())
                    .first()) {
                is ApiResponse.Empty -> {}
                is ApiResponse.Success -> {
                    val result = response.data.gudang
                    emit(Resource.Success(result, response.data.message))
                }
                is ApiResponse.Error -> emit(Resource.Error(response.errorMessage))
            }
        }.catch {
            emit(Resource.Error(it.message.toString()))
        }.flowOn(Dispatchers.IO)


    override suspend fun getKendaraanById(id: String): Flow<Resource<Kendaraan>> =
        flow {
            emit(Resource.Loading())
            when (val response =
                kendaraanRemoteDataSource.getKendaraanById(storageDataSource.getToken(), id)
                    .first()) {
                is ApiResponse.Empty -> {}
                is ApiResponse.Success -> {
                    val result = response.data.kendaraan
                    emit(Resource.Success(result, response.data.message))
                }

                is ApiResponse.Error -> emit(Resource.Error(response.errorMessage))
            }
        }.catch {
            emit(Resource.Error(it.message.toString()))
        }.flowOn(Dispatchers.IO)

    override suspend fun getActiveDeliveryOrderByKendaraan(id: String): Flow<Resource<List<AllDeliveryOrder>>> =
        flow {
            emit(Resource.Loading())
            when (val response =
                kendaraanRemoteDataSource.getActiveDeliveryOrderByKendaraan(
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

    override suspend fun getActiveSuratJalanByKendaraan(
        id: String,
        tipe: SuratJalanTipe
    ): Flow<Resource<List<AllSuratJalan>>> =
        flow {
            emit(Resource.Loading())
            when (val response =
                kendaraanRemoteDataSource.getActiveSuratJalanByKendaraan(
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

    override suspend fun addKendaraan(
        gudangId: String,
        jenis: String,
        merk: String,
        platNomor: String,
        gambar: File
    ): Flow<Resource<String>> =
        flow {
            emit(Resource.Loading())
            when (val response =
                kendaraanRemoteDataSource.addKendaraan(
                    storageDataSource.getToken(),
                    gudangId,
                    jenis,
                    merk,
                    platNomor,
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

    override suspend fun deleteKendaraan(id: String): Flow<Resource<ErrorMessageResponse>> =
        flow {
            emit(Resource.Loading())
            when (val response =
                kendaraanRemoteDataSource.deleteKendaraan(storageDataSource.getToken(), id)
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

    override suspend fun deletePengendara(id: String): Flow<Resource<ErrorMessageResponse>> =
        flow {
            emit(Resource.Loading())
            when (val response =
                kendaraanRemoteDataSource.deletePengendara(storageDataSource.getToken(), id)
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

    override suspend fun updateKendaraan(
        id: String,
        gudangId: String,
        jenis: String,
        merk: String,
        platNomor: String,
        gambar: File?
    ): Flow<Resource<String>> =
        flow {
            emit(Resource.Loading())
            when (val response =
                kendaraanRemoteDataSource.updateKendaraan(
                    storageDataSource.getToken(),
                    id,
                    gudangId,
                    jenis,
                    merk,
                    platNomor,
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

