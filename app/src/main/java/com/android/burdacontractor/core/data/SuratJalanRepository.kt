package com.android.burdacontractor.core.data

import androidx.paging.PagingData
import com.android.burdacontractor.core.data.source.local.StorageDataSource
import com.android.burdacontractor.core.data.source.remote.SuratJalanRemoteDataSource
import com.android.burdacontractor.core.data.source.remote.response.AddSuratJalanResponse
import com.android.burdacontractor.core.data.source.remote.response.ErrorMessageResponse
import com.android.burdacontractor.core.data.source.remote.response.SuratJalanDetailItem
import com.android.burdacontractor.core.data.source.remote.response.SuratJalanItem
import com.android.burdacontractor.core.domain.model.SuratJalanStatus
import com.android.burdacontractor.core.domain.model.SuratJalanTipe
import com.android.burdacontractor.core.domain.repository.ISuratJalanRepository
import kotlinx.coroutines.flow.Flow
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
    ): Flow<PagingData<SuratJalanItem>> =
        suratJalanRemoteDataSource.getAllSuratJalan(
            storageDataSource.getToken() ,tipe,status,date_start,date_end,size,search
        )

    override suspend fun getSuratJalanById(id: String): Flow<Resource<SuratJalanDetailItem>> {
        TODO("Not yet implemented")
    }

    override suspend fun addSuratJalanPengirimanGp(
        adminGudangId: String,
        logisticId: String,
        kendaraanId: String,
        peminjamanId: String
    ): Flow<Resource<AddSuratJalanResponse>> {
        TODO("Not yet implemented")
    }

    override suspend fun addSuratJalanPengirimanPp(
        adminGudangId: String,
        logisticId: String,
        kendaraanId: String,
        peminjamanAsalId: String,
        peminjamanTujuanId: String
    ): Flow<Resource<AddSuratJalanResponse>> {
        TODO("Not yet implemented")
    }

    override suspend fun addSuratJalanPengembalian(
        adminGudangId: String,
        logisticId: String,
        kendaraanId: String,
        pengembalianId: String
    ): Flow<Resource<AddSuratJalanResponse>> {
        TODO("Not yet implemented")
    }

    override suspend fun updateSuratJalanPengirimanGp(
        adminGudangId: String,
        logisticId: String,
        kendaraanId: String,
        peminjamanId: String
    ): Flow<Resource<AddSuratJalanResponse>> {
        TODO("Not yet implemented")
    }

    override suspend fun updateSuratJalanPengirimanGp(
        adminGudangId: String,
        logisticId: String,
        kendaraanId: String,
        peminjamanAsalId: String,
        peminjamanTujuanId: String
    ): Flow<Resource<AddSuratJalanResponse>> {
        TODO("Not yet implemented")
    }

    override suspend fun updateSuratJalanPengembalian(
        adminGudangId: String,
        logisticId: String,
        kendaraanId: String,
        pengembalianId: String
    ): Flow<Resource<AddSuratJalanResponse>> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteSuratJalan(suratJalanId: String): Flow<Resource<ErrorMessageResponse>> {
        TODO("Not yet implemented")
    }

}

