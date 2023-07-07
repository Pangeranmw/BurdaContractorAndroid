package com.android.burdacontractor.feature.suratjalan.domain.repository

import androidx.paging.PagingData
import com.android.burdacontractor.core.data.Resource
import com.android.burdacontractor.core.data.source.remote.response.ErrorMessageResponse
import com.android.burdacontractor.core.domain.model.CountActive
import com.android.burdacontractor.core.domain.model.enums.SuratJalanStatus
import com.android.burdacontractor.core.domain.model.enums.SuratJalanTipe
import com.android.burdacontractor.feature.suratjalan.domain.model.AllSuratJalan
import com.android.burdacontractor.feature.suratjalan.domain.model.DataAllDeliveryOrderWithCount
import com.android.burdacontractor.feature.suratjalan.domain.model.SuratJalanDetail
import kotlinx.coroutines.flow.Flow

interface ISuratJalanRepository {

    fun getAllSuratJalan(
        tipe: SuratJalanTipe,
        status: SuratJalanStatus,
        date_start: String? = null,
        date_end: String? = null,
        size: Int = 5,
        search: String? = null,
    ): Flow<PagingData<AllSuratJalan>>

    suspend fun getSuratJalanById(id: String): Flow<Resource<SuratJalanDetail>>

    suspend fun getSomeActiveSuratJalan(tipe: SuratJalanTipe, size: Int = 5): Flow<Resource<DataAllDeliveryOrderWithCount>>
    suspend fun getAllSuratJalanDalamPerjalananByUser(): Flow<Resource<List<AllSuratJalan>>>
    suspend fun getCountActiveSuratJalan(): Flow<Resource<CountActive>>

    suspend fun addSuratJalanPengirimanGp(
        adminGudangId: String,
        logisticId: String,
        kendaraanId: String,
        peminjamanId: String
    ): Flow<Resource<ErrorMessageResponse>>

    suspend fun addSuratJalanPengirimanPp(
        adminGudangId: String,
        logisticId: String,
        kendaraanId: String,
        peminjamanAsalId: String,
        peminjamanTujuanId: String,
    ): Flow<Resource<ErrorMessageResponse>>

    suspend fun addSuratJalanPengembalian(
        adminGudangId: String,
        logisticId: String,
        kendaraanId: String,
        pengembalianId: String,
    ): Flow<Resource<ErrorMessageResponse>>

    suspend fun updateSuratJalanPengirimanGp(
        adminGudangId: String,
        logisticId: String,
        kendaraanId: String,
        peminjamanId: String
    ): Flow<Resource<ErrorMessageResponse>>

    suspend fun updateSuratJalanPengirimanPp(
        adminGudangId: String,
        logisticId: String,
        kendaraanId: String,
        peminjamanAsalId: String,
        peminjamanTujuanId: String,
    ): Flow<Resource<ErrorMessageResponse>>

    suspend fun updateSuratJalanPengembalian(
        adminGudangId: String,
        logisticId: String,
        kendaraanId: String,
        pengembalianId: String,
    ): Flow<Resource<ErrorMessageResponse>>

    suspend fun deleteSuratJalan(
        suratJalanId: String,
    ): Flow<Resource<ErrorMessageResponse>>
}