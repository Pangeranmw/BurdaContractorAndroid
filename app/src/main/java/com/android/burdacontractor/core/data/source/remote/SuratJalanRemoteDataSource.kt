package com.android.burdacontractor.core.data.source.remote

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.android.burdacontractor.core.data.Resource
import com.android.burdacontractor.core.data.source.local.storage.SessionManager
import com.android.burdacontractor.core.data.source.remote.network.SuratJalanService
import com.android.burdacontractor.core.data.source.remote.response.AddSuratJalanResponse
import com.android.burdacontractor.core.data.source.remote.response.ErrorMessageResponse
import com.android.burdacontractor.core.data.source.remote.response.SuratJalanDetailItem
import com.android.burdacontractor.core.data.source.remote.response.SuratJalanItem
import com.android.burdacontractor.core.domain.model.enum.SuratJalanStatus
import com.android.burdacontractor.core.domain.model.enum.SuratJalanTipe
import com.google.firebase.database.DatabaseReference
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SuratJalanRemoteDataSource @Inject constructor(
    private val suratJalanService: SuratJalanService,
    private val sessionManager: SessionManager,
    private val databaseReference: DatabaseReference,
) {

    suspend fun getAllSuratJalan(
        tipe: SuratJalanTipe,
        status: SuratJalanStatus,
        date_start: String? = null,
        date_end: String? = null,
        size: Int = 5,
        search: String? = null,
    ): Flow<PagingData<SuratJalanItem>> {
        val token = sessionManager.getFromPreference(SessionManager.KEY_TOKEN)
        return Pager(
            config = PagingConfig(
                pageSize = size
            ),
            pagingSourceFactory = {
                SuratJalanPagingSource(suratJalanService, token.toString(), tipe.name, status.name, date_start, date_end, size, search)
            }
        ).flow
    }

    suspend fun getSuratJalanById(id: String): Flow<Resource<SuratJalanDetailItem>> = flow{

    }

    suspend fun addSuratJalanPengirimanGp(
        adminGudangId: String,
        logisticId: String,
        kendaraanId: String,
        peminjamanId: String
    ): Flow<Resource<AddSuratJalanResponse>> = flow{

    }

    suspend fun addSuratJalanPengirimanPp(
        adminGudangId: String,
        logisticId: String,
        kendaraanId: String,
        peminjamanAsalId: String,
        peminjamanTujuanId: String,
    ): Flow<Resource<AddSuratJalanResponse>> = flow{

    }

    suspend fun addSuratJalanPengembalian(
        adminGudangId: String,
        logisticId: String,
        kendaraanId: String,
        pengembalianId: String,
    ): Flow<Resource<AddSuratJalanResponse>> = flow {

    }

    suspend fun updateSuratJalanPengirimanGp(
        adminGudangId: String,
        logisticId: String,
        kendaraanId: String,
        peminjamanId: String
    ): Flow<Resource<AddSuratJalanResponse>> = flow{

    }

    suspend fun updateSuratJalanPengirimanGp(
        adminGudangId: String,
        logisticId: String,
        kendaraanId: String,
        peminjamanAsalId: String,
        peminjamanTujuanId: String,
    ): Flow<Resource<AddSuratJalanResponse>> = flow{

    }

    suspend fun updateSuratJalanPengembalian(
        adminGudangId: String,
        logisticId: String,
        kendaraanId: String,
        pengembalianId: String,
    ): Flow<Resource<AddSuratJalanResponse>> = flow{

    }

    suspend fun deleteSuratJalan(
        suratJalanId: String,
    ): Flow<Resource<ErrorMessageResponse>> = flow{

    }
}

