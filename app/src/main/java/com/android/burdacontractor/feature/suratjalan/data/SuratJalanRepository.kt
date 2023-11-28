package com.android.burdacontractor.feature.suratjalan.data

import androidx.paging.PagingData
import com.android.burdacontractor.core.data.Resource
import com.android.burdacontractor.core.data.source.local.StorageDataSource
import com.android.burdacontractor.core.data.source.remote.network.ApiResponse
import com.android.burdacontractor.core.data.source.remote.response.CountActiveResponse
import com.android.burdacontractor.core.data.source.remote.response.ErrorMessageResponse
import com.android.burdacontractor.core.domain.model.Proyek
import com.android.burdacontractor.core.domain.model.enums.CreatedByOrFor
import com.android.burdacontractor.core.domain.model.enums.SuratJalanStatus
import com.android.burdacontractor.core.domain.model.enums.SuratJalanTipe
import com.android.burdacontractor.feature.suratjalan.data.source.remote.SuratJalanRemoteDataSource
import com.android.burdacontractor.feature.suratjalan.data.source.remote.request.AddUpdateSuratJalanBody
import com.android.burdacontractor.feature.suratjalan.data.source.remote.response.DataAllSuratJalanWithCountItem
import com.android.burdacontractor.feature.suratjalan.data.source.remote.response.StatisticCountTitleItem
import com.android.burdacontractor.feature.suratjalan.domain.model.AllSuratJalan
import com.android.burdacontractor.feature.suratjalan.domain.model.PeminjamanSuratJalan
import com.android.burdacontractor.feature.suratjalan.domain.model.PenggunaanSuratJalan
import com.android.burdacontractor.feature.suratjalan.domain.model.SuratJalanDetailItem
import com.android.burdacontractor.feature.suratjalan.domain.repository.ISuratJalanRepository
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
class SuratJalanRepository @Inject constructor(
    private val suratJalanRemoteDataSource: SuratJalanRemoteDataSource,
    private val storageDataSource: StorageDataSource
) : ISuratJalanRepository {

    override fun getAllSuratJalan(
        tipe: SuratJalanTipe,
        status: SuratJalanStatus,
        dateStart: String?,
        dateEnd: String?,
        size: Int,
        search: String?,
        createdByOrFor: CreatedByOrFor,
    ): Flow<PagingData<AllSuratJalan>> =
        suratJalanRemoteDataSource.getAllSuratJalan(
            storageDataSource.getToken(),
            tipe,
            status,
            dateStart,
            dateEnd,
            size,
            search,
            createdByOrFor
        )

    override suspend fun getSuratJalanById(id: String): Flow<Resource<SuratJalanDetailItem>> =
        flow {
            emit(Resource.Loading())
            when (val response =
                suratJalanRemoteDataSource.getSuratJalanById(storageDataSource.getToken(), id)
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

    override suspend fun getAvailableProyekBySuratJalanType(tipe: String): Flow<Resource<List<Proyek>>> =
        flow {
            emit(Resource.Loading())
            when (val response =
                suratJalanRemoteDataSource.getAvailableProyekBySuratJalanType(
                    storageDataSource.getToken(),
                    tipe
                )
                    .first()) {
                is ApiResponse.Empty -> {}
                is ApiResponse.Success -> {
                    val result = response.data.proyek
                    emit(Resource.Success(result, response.data.message))
                }

                is ApiResponse.Error -> emit(Resource.Error(response.errorMessage))
            }
        }.catch {
            emit(Resource.Error(it.message.toString()))
        }.flowOn(Dispatchers.IO)

    override suspend fun getAvailablePeminjamanByProyek(
        tipe: String,
        proyekId: String
    ): Flow<Resource<List<PeminjamanSuratJalan>>> =
        flow {
            emit(Resource.Loading())
            when (val response =
                suratJalanRemoteDataSource.getAvailablePeminjamanByProyek(
                    storageDataSource.getToken(),
                    tipe,
                    proyekId
                )
                    .first()) {
                is ApiResponse.Empty -> {}
                is ApiResponse.Success -> {
                    val result = response.data.peminjaman
                    emit(Resource.Success(result, response.data.message))
                }

                is ApiResponse.Error -> emit(Resource.Error(response.errorMessage))
            }
        }.catch {
            emit(Resource.Error(it.message.toString()))
        }.flowOn(Dispatchers.IO)

    override suspend fun getAvailablePenggunaanByProyek(
        tipe: String,
        proyekId: String
    ): Flow<Resource<List<PenggunaanSuratJalan>>> =
        flow {
            emit(Resource.Loading())
            when (val response =
                suratJalanRemoteDataSource.getAvailablePenggunaanByProyek(
                    storageDataSource.getToken(),
                    tipe,
                    proyekId
                )
                    .first()) {
                is ApiResponse.Empty -> {}
                is ApiResponse.Success -> {
                    val result = response.data.penggunaan
                    emit(Resource.Success(result, response.data.message))
                }

                is ApiResponse.Error -> emit(Resource.Error(response.errorMessage))
            }
        }.catch {
            emit(Resource.Error(it.message.toString()))
        }.flowOn(Dispatchers.IO)

    override suspend fun getAllSuratJalanDalamPerjalananByUser(): Flow<Resource<List<AllSuratJalan>>> =
        flow {
            emit(Resource.Loading())
            when (val response =
                suratJalanRemoteDataSource.getAllSuratJalanDalamPerjalananByUser(storageDataSource.getToken())
                    .first()) {
                is ApiResponse.Empty -> {}
                is ApiResponse.Success -> {
                    val result = response.data.suratJalan!!
                    emit(Resource.Success(result, response.data.message))
                }

                is ApiResponse.Error -> emit(Resource.Error(response.errorMessage))
            }
        }.catch {
            emit(Resource.Error(it.message.toString()))
        }.flowOn(Dispatchers.IO)

    override suspend fun getSomeActiveSuratJalan(
        tipe: SuratJalanTipe,
        size: Int
    ): Flow<Resource<DataAllSuratJalanWithCountItem>> =
        flow {
            emit(Resource.Loading())
            when (val response = suratJalanRemoteDataSource.getAllSuratJalanWithCount(
                storageDataSource.getToken(),
                tipe,
                size
            ).first()) {
                is ApiResponse.Empty -> {}
                is ApiResponse.Success -> {
                    val result = response.data.data!!
                    emit(Resource.Success(result, response.data.message))
                }
                is ApiResponse.Error -> emit(Resource.Error(response.errorMessage))
            }
        }.catch {
            emit(Resource.Error(it.message.toString()))
        }.flowOn(Dispatchers.IO)

    override suspend fun getStatistikMenungguSuratJalan(): Flow<Resource<List<StatisticCountTitleItem>>> =
        flow {
            emit(Resource.Loading())
            when (val response =
                suratJalanRemoteDataSource.getStatistikMenungguSuratJalan(storageDataSource.getToken())
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

    override suspend fun getCountActiveSuratJalan(): Flow<Resource<CountActiveResponse>> =
        flow {
            emit(Resource.Loading())
            when (val response =
                suratJalanRemoteDataSource.getCountActiveSuratJalan(storageDataSource.getToken())
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

    override suspend fun deleteSuratJalan(id: String): Flow<Resource<ErrorMessageResponse>> =
        flow {
            emit(Resource.Loading())
            when (val response =
                suratJalanRemoteDataSource.deleteSuratJalan(storageDataSource.getToken(), id)
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

    override suspend fun sendSuratJalan(id: String): Flow<Resource<ErrorMessageResponse>> =
        flow {
            emit(Resource.Loading())
            when (val response =
                suratJalanRemoteDataSource.sendSuratJalan(storageDataSource.getToken(), id)
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

    override suspend fun deleteSuratJalanChild(
        sjChildId: String,
        tipe: String
    ): Flow<Resource<ErrorMessageResponse>> =
        flow {
            emit(Resource.Loading())
            when (val response =
                suratJalanRemoteDataSource.deleteSuratJalanChild(
                    storageDataSource.getToken(),
                    sjChildId,
                    tipe
                )
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

    override suspend fun addSuratJalan(
        addUpdateSuratJalanBody: AddUpdateSuratJalanBody
    ): Flow<Resource<String>> =
        flow {
            emit(Resource.Loading())
            when (val response =
                suratJalanRemoteDataSource.addSuratJalan(
                    storageDataSource.getToken(),
                    addUpdateSuratJalanBody
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

    override suspend fun updateSuratJalan(
        id: String,
        addUpdateSuratJalanBody: AddUpdateSuratJalanBody
    ): Flow<Resource<String>> =
        flow {
            emit(Resource.Loading())
            when (val response =
                suratJalanRemoteDataSource.updateSuratJalan(
                    storageDataSource.getToken(),
                    id,
                    addUpdateSuratJalanBody
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

    override suspend fun giveTtdSuratJalan(id: String): Flow<Resource<ErrorMessageResponse>> =
        flow {
            emit(Resource.Loading())
            when (val response =
                suratJalanRemoteDataSource.giveTtdSuratJalan(storageDataSource.getToken(), id)
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

    override suspend fun uploadFotoBuktiSuratJalan(
        id: String,
        fotoBukti: File
    ): Flow<Resource<ErrorMessageResponse>> =
        flow {
            emit(Resource.Loading())
            when (val response =
                suratJalanRemoteDataSource.uploadFotoBuktiSuratJalan(
                    storageDataSource.getToken(),
                    id,
                    fotoBukti
                )
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

    override suspend fun markCompleteSuratJalan(id: String): Flow<Resource<ErrorMessageResponse>> =
        flow {
            emit(Resource.Loading())
            when (val response =
                suratJalanRemoteDataSource.markCompleteSuratJalan(storageDataSource.getToken(), id)
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

}

