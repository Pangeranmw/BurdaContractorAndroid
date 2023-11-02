package com.android.burdacontractor.feature.kendaraan.domain.repository

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.android.burdacontractor.core.data.Resource
import com.android.burdacontractor.core.data.source.remote.response.ErrorMessageResponse
import com.android.burdacontractor.core.domain.model.enums.SuratJalanTipe
import com.android.burdacontractor.feature.deliveryorder.domain.model.AllDeliveryOrder
import com.android.burdacontractor.feature.gudang.domain.model.GudangById
import com.android.burdacontractor.feature.kendaraan.domain.model.AllKendaraan
import com.android.burdacontractor.feature.kendaraan.domain.model.Kendaraan
import com.android.burdacontractor.feature.kendaraan.domain.model.KendaraanByLogistic
import com.android.burdacontractor.feature.suratjalan.domain.model.AllSuratJalan
import kotlinx.coroutines.flow.Flow
import java.io.File

interface IKendaraanRepository {

    suspend fun getKendaraanByLogistic(): Flow<Resource<KendaraanByLogistic>>
    suspend fun getKendaraanGudang(): Flow<Resource<List<GudangById>>>
    suspend fun getKendaraanById(id: String): Flow<Resource<Kendaraan>>
    suspend fun getActiveDeliveryOrderByKendaraan(id: String): Flow<Resource<List<AllDeliveryOrder>>>
    suspend fun getActiveSuratJalanByKendaraan(
        id: String,
        tipe: SuratJalanTipe
    ): Flow<Resource<List<AllSuratJalan>>>

    fun getAllKendaraan(
        size: Int = 5,
        filter: String? = null,
        gudang: String? = null,
        status: String? = null,
        search: String? = null,
    ): LiveData<PagingData<AllKendaraan>>

    suspend fun addKendaraan(
        gudangId: String,
        jenis: String,
        merk: String,
        platNomor: String,
        gambar: File,
    ): Flow<Resource<String>>

    suspend fun deleteKendaraan(
        id: String,
    ): Flow<Resource<ErrorMessageResponse>>

    suspend fun deletePengendara(
        id: String,
    ): Flow<Resource<ErrorMessageResponse>>

    suspend fun updateKendaraan(
        id: String,
        gudangId: String,
        jenis: String,
        merk: String,
        platNomor: String,
        gambar: File? = null,
    ): Flow<Resource<String>>
}