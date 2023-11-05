package com.android.burdacontractor.feature.gudang.domain.repository

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.android.burdacontractor.core.data.Resource
import com.android.burdacontractor.core.data.source.remote.response.ErrorMessageResponse
import com.android.burdacontractor.feature.deliveryorder.domain.model.AllDeliveryOrder
import com.android.burdacontractor.feature.gudang.domain.model.AllGudang
import com.android.burdacontractor.feature.gudang.domain.model.GudangById
import com.android.burdacontractor.feature.suratjalan.data.source.remote.response.StatisticCountTitleItem
import com.android.burdacontractor.feature.suratjalan.domain.model.AllSuratJalan
import kotlinx.coroutines.flow.Flow
import java.io.File

interface IGudangRepository {
    suspend fun getGudangById(id: String): Flow<Resource<GudangById>>
    suspend fun getGudangProvinsi(): Flow<Resource<List<String>>>
    fun getAllGudang(
        size: Int = 5,
        search: String? = null,
        filter: String? = null,
        coordinate: String? = null,
    ): LiveData<PagingData<AllGudang>>

    suspend fun getStatistikDeliveryOrderByGudang(id: String): Flow<Resource<List<StatisticCountTitleItem>>>
    suspend fun getActiveDeliveryOrderByGudang(id: String): Flow<Resource<List<AllDeliveryOrder>>>
    suspend fun getStatistikSuratJalanByGudang(
        id: String,
        tipe: String
    ): Flow<Resource<List<StatisticCountTitleItem>>>

    suspend fun getActiveSuratJalanByGudang(
        id: String,
        tipe: String
    ): Flow<Resource<List<AllSuratJalan>>>

    suspend fun addGudang(
        nama: String,
        alamat: String,
        provinsi: String,
        kota: String,
        latitude: String,
        longitude: String,
        gambar: File,
    ): Flow<Resource<String>>

    suspend fun updateGudang(
        id: String,
        nama: String,
        alamat: String,
        provinsi: String,
        kota: String,
        latitude: String,
        longitude: String,
        gambar: File?,
    ): Flow<Resource<String>>

    suspend fun deleteGudang(
        id: String
    ): Flow<Resource<ErrorMessageResponse>>
}