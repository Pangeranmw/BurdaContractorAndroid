package com.android.burdacontractor.core.data

import androidx.paging.PagingData
import com.android.burdacontractor.core.data.source.local.StorageDataSource
import com.android.burdacontractor.core.data.source.remote.SuratJalanRemoteDataSource
import com.android.burdacontractor.core.data.source.remote.network.ApiResponse
import com.android.burdacontractor.core.data.source.remote.response.AddUpdateResponse
import com.android.burdacontractor.core.data.source.remote.response.ErrorMessageResponse
import com.android.burdacontractor.core.data.source.remote.response.sjDetailtoDomain
import com.android.burdacontractor.core.domain.model.AllSuratJalan
import com.android.burdacontractor.core.domain.model.SuratJalanDetail
import com.android.burdacontractor.core.domain.model.enums.SuratJalanStatus
import com.android.burdacontractor.core.domain.model.enums.SuratJalanTipe
import com.android.burdacontractor.core.domain.repository.ISuratJalanRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SuratJalanRepository @Inject constructor(
    private val suratJalanRemoteDataSource: SuratJalanRemoteDataSource,
    private val storageDataSource: StorageDataSource
) : ISuratJalanRepository {

    override suspend fun getAllSuratJalan(
        tipe: SuratJalanTipe,
        status: SuratJalanStatus,
        date_start: String?,
        date_end: String?,
        size: Int,
        search: String?
    ): Flow<PagingData<AllSuratJalan>> =
        suratJalanRemoteDataSource.getAllSuratJalan(
            storageDataSource.getToken(),tipe,status,date_start,date_end,size,search
        )

    override suspend fun getSuratJalanById(id: String): Flow<Resource<SuratJalanDetail>> = flow{
        when(val response = suratJalanRemoteDataSource.getSuratJalanById(storageDataSource.getToken(), id).first()){
            is ApiResponse.Empty -> emit(Resource.Loading())
            is ApiResponse.Success -> emit(Resource.Success(response.data.suratJalan.sjDetailtoDomain()))
            is ApiResponse.Error -> emit(Resource.Error(response.errorMessage))
        }
    }

    override suspend fun addSuratJalanPengirimanGp(
        adminGudangId: String,
        logisticId: String,
        kendaraanId: String,
        peminjamanId: String
    ): Flow<Resource<AddUpdateResponse>> {
        TODO("Not yet implemented")
    }

    override suspend fun addSuratJalanPengirimanPp(
        adminGudangId: String,
        logisticId: String,
        kendaraanId: String,
        peminjamanAsalId: String,
        peminjamanTujuanId: String
    ): Flow<Resource<AddUpdateResponse>> {
        TODO("Not yet implemented")
    }

    override suspend fun addSuratJalanPengembalian(
        adminGudangId: String,
        logisticId: String,
        kendaraanId: String,
        pengembalianId: String
    ): Flow<Resource<AddUpdateResponse>> {
        TODO("Not yet implemented")
    }

    override suspend fun updateSuratJalanPengirimanGp(
        adminGudangId: String,
        logisticId: String,
        kendaraanId: String,
        peminjamanId: String
    ): Flow<Resource<AddUpdateResponse>> {
        TODO("Not yet implemented")
    }

    override suspend fun updateSuratJalanPengirimanGp(
        adminGudangId: String,
        logisticId: String,
        kendaraanId: String,
        peminjamanAsalId: String,
        peminjamanTujuanId: String
    ): Flow<Resource<AddUpdateResponse>> {
        TODO("Not yet implemented")
    }

    override suspend fun updateSuratJalanPengembalian(
        adminGudangId: String,
        logisticId: String,
        kendaraanId: String,
        pengembalianId: String
    ): Flow<Resource<AddUpdateResponse>> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteSuratJalan(suratJalanId: String): Flow<Resource<ErrorMessageResponse>> {
        TODO("Not yet implemented")
    }

}

