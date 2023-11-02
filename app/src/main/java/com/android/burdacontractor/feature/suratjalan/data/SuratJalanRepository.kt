package com.android.burdacontractor.feature.suratjalan.data

import androidx.paging.PagingData
import com.android.burdacontractor.core.data.Resource
import com.android.burdacontractor.core.data.source.local.StorageDataSource
import com.android.burdacontractor.core.data.source.remote.network.ApiResponse
import com.android.burdacontractor.core.data.source.remote.response.CountActiveResponse
import com.android.burdacontractor.core.data.source.remote.response.ErrorMessageResponse
import com.android.burdacontractor.core.domain.model.enums.SuratJalanStatus
import com.android.burdacontractor.core.domain.model.enums.SuratJalanTipe
import com.android.burdacontractor.feature.suratjalan.data.source.remote.SuratJalanRemoteDataSource
import com.android.burdacontractor.feature.suratjalan.data.source.remote.response.DataAllSuratJalanWithCountItem
import com.android.burdacontractor.feature.suratjalan.data.source.remote.response.StatisticCountTitleItem
import com.android.burdacontractor.feature.suratjalan.data.source.remote.response.SuratJalanDetailItem
import com.android.burdacontractor.feature.suratjalan.data.source.remote.response.SuratJalanItem
import com.android.burdacontractor.feature.suratjalan.domain.repository.ISuratJalanRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
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
        date_start: String?,
        date_end: String?,
        size: Int,
        search: String?
    ): Flow<PagingData<SuratJalanItem>> =
        suratJalanRemoteDataSource.getAllSuratJalan(
            storageDataSource.getToken(), tipe, status, date_start, date_end, size, search
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

    override suspend fun getAllSuratJalanDalamPerjalananByUser(): Flow<Resource<List<SuratJalanItem>>> =
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

    override suspend fun addSuratJalanPengirimanGp(
        adminGudangId: String,
        logisticId: String,
        kendaraanId: String,
        peminjamanId: String
    ): Flow<Resource<ErrorMessageResponse>> {
        TODO("Not yet implemented")
    }

    override suspend fun addSuratJalanPengirimanPp(
        adminGudangId: String,
        logisticId: String,
        kendaraanId: String,
        peminjamanAsalId: String,
        peminjamanTujuanId: String
    ): Flow<Resource<ErrorMessageResponse>> {
        TODO("Not yet implemented")
    }

    override suspend fun addSuratJalanPengembalian(
        adminGudangId: String,
        logisticId: String,
        kendaraanId: String,
        pengembalianId: String
    ): Flow<Resource<ErrorMessageResponse>> {
        TODO("Not yet implemented")
    }

    override suspend fun updateSuratJalanPengirimanGp(
        adminGudangId: String,
        logisticId: String,
        kendaraanId: String,
        peminjamanId: String
    ): Flow<Resource<ErrorMessageResponse>> {
        TODO("Not yet implemented")
    }

    override suspend fun updateSuratJalanPengirimanPp(
        adminGudangId: String,
        logisticId: String,
        kendaraanId: String,
        peminjamanAsalId: String,
        peminjamanTujuanId: String
    ): Flow<Resource<ErrorMessageResponse>> {
        TODO("Not yet implemented")
    }

    override suspend fun updateSuratJalanPengembalian(
        adminGudangId: String,
        logisticId: String,
        kendaraanId: String,
        pengembalianId: String
    ): Flow<Resource<ErrorMessageResponse>> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteSuratJalan(suratJalanId: String): Flow<Resource<ErrorMessageResponse>> {
        TODO("Not yet implemented")
    }
}

