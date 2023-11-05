package com.android.burdacontractor.feature.perusahaan.domain.repository

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.android.burdacontractor.core.data.Resource
import com.android.burdacontractor.core.data.source.remote.response.ErrorMessageResponse
import com.android.burdacontractor.feature.deliveryorder.domain.model.AllDeliveryOrder
import com.android.burdacontractor.feature.perusahaan.domain.model.AllPerusahaan
import com.android.burdacontractor.feature.perusahaan.domain.model.PerusahaanById
import com.android.burdacontractor.feature.suratjalan.data.source.remote.response.StatisticCountTitleItem
import kotlinx.coroutines.flow.Flow
import java.io.File

interface IPerusahaanRepository {
    suspend fun getPerusahaanById(id: String): Flow<Resource<PerusahaanById>>
    suspend fun getPerusahaanProvinsi(): Flow<Resource<List<String>>>
    suspend fun getStatistikDeliveryOrderByPerusahaan(id: String): Flow<Resource<List<StatisticCountTitleItem>>>
    suspend fun getActiveDeliveryOrderByPerusahaan(id: String): Flow<Resource<List<AllDeliveryOrder>>>
    suspend fun addPerusahaan(
        nama: String,
        alamat: String,
        provinsi: String,
        kota: String,
        latitude: String,
        longitude: String,
        gambar: File,
    ): Flow<Resource<String>>

    suspend fun updatePerusahaan(
        id: String,
        nama: String,
        alamat: String,
        provinsi: String,
        kota: String,
        latitude: String,
        longitude: String,
        gambar: File?,
    ): Flow<Resource<String>>

    suspend fun deletePerusahaan(
        id: String
    ): Flow<Resource<ErrorMessageResponse>>

    fun getAllPerusahaan(
        size: Int = 5,
        search: String? = null,
        filter: String? = null,
        coordinate: String? = null,
    ): LiveData<PagingData<AllPerusahaan>>
}
