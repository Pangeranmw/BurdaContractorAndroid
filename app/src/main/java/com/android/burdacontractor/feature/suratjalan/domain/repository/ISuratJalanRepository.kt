package com.android.burdacontractor.feature.suratjalan.domain.repository

import androidx.paging.PagingData
import com.android.burdacontractor.core.data.Resource
import com.android.burdacontractor.core.data.source.remote.response.CountActiveResponse
import com.android.burdacontractor.core.data.source.remote.response.ErrorMessageResponse
import com.android.burdacontractor.core.domain.model.Proyek
import com.android.burdacontractor.core.domain.model.enums.CreatedByOrFor
import com.android.burdacontractor.core.domain.model.enums.SuratJalanStatus
import com.android.burdacontractor.core.domain.model.enums.SuratJalanTipe
import com.android.burdacontractor.feature.suratjalan.data.source.remote.request.AddUpdateSuratJalanBody
import com.android.burdacontractor.feature.suratjalan.data.source.remote.response.DataAllSuratJalanWithCountItem
import com.android.burdacontractor.feature.suratjalan.data.source.remote.response.StatisticCountTitleItem
import com.android.burdacontractor.feature.suratjalan.domain.model.AllSuratJalan
import com.android.burdacontractor.feature.suratjalan.domain.model.PeminjamanSuratJalan
import com.android.burdacontractor.feature.suratjalan.domain.model.PenggunaanSuratJalan
import com.android.burdacontractor.feature.suratjalan.domain.model.SuratJalanDetailItem
import kotlinx.coroutines.flow.Flow
import java.io.File

interface ISuratJalanRepository {

    fun getAllSuratJalan(
        tipe: SuratJalanTipe,
        status: SuratJalanStatus,
        dateStart: String? = null,
        dateEnd: String? = null,
        size: Int = 5,
        search: String? = null,
        createdByOrFor: CreatedByOrFor,
    ): Flow<PagingData<AllSuratJalan>>

    suspend fun getSuratJalanById(id: String): Flow<Resource<SuratJalanDetailItem>>
    suspend fun getAvailableProyekBySuratJalanType(tipe: String): Flow<Resource<List<Proyek>>>
    suspend fun getAvailablePeminjamanByProyek(
        tipe: String,
        proyekId: String
    ): Flow<Resource<List<PeminjamanSuratJalan>>>

    suspend fun getAvailablePenggunaanByProyek(
        tipe: String,
        proyekId: String
    ): Flow<Resource<List<PenggunaanSuratJalan>>>

    suspend fun getSomeActiveSuratJalan(
        tipe: SuratJalanTipe,
        size: Int = 5
    ): Flow<Resource<DataAllSuratJalanWithCountItem>>

    suspend fun getStatistikMenungguSuratJalan(): Flow<Resource<List<StatisticCountTitleItem>>>
    suspend fun getAllSuratJalanDalamPerjalananByUser(): Flow<Resource<List<AllSuratJalan>>>
    suspend fun getCountActiveSuratJalan(): Flow<Resource<CountActiveResponse>>
    suspend fun deleteSuratJalan(
        id: String,
    ): Flow<Resource<ErrorMessageResponse>>

    suspend fun sendSuratJalan(
        id: String,
    ): Flow<Resource<ErrorMessageResponse>>

    suspend fun markCompleteSuratJalan(
        id: String,
    ): Flow<Resource<ErrorMessageResponse>>

    suspend fun giveTtdSuratJalan(
        id: String,
    ): Flow<Resource<ErrorMessageResponse>>

    suspend fun uploadFotoBuktiSuratJalan(
        id: String,
        fotoBukti: File
    ): Flow<Resource<ErrorMessageResponse>>

    suspend fun deleteSuratJalanChild(
        sjChildId: String,
        tipe: String,
    ): Flow<Resource<ErrorMessageResponse>>

    suspend fun addSuratJalan(
        addUpdateSuratJalanBody: AddUpdateSuratJalanBody
    ): Flow<Resource<String>>

    suspend fun updateSuratJalan(
        id: String,
        addUpdateSuratJalanBody: AddUpdateSuratJalanBody
    ): Flow<Resource<String>>
}