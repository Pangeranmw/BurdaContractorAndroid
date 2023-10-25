package com.android.burdacontractor.feature.kendaraan.domain.repository

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.android.burdacontractor.core.data.Resource
import com.android.burdacontractor.feature.gudang.domain.model.GudangById
import com.android.burdacontractor.feature.kendaraan.domain.model.AllKendaraan
import com.android.burdacontractor.feature.kendaraan.domain.model.Kendaraan
import com.android.burdacontractor.feature.kendaraan.domain.model.KendaraanByLogistic
import kotlinx.coroutines.flow.Flow

interface IKendaraanRepository {

    suspend fun getKendaraanByLogistic(): Flow<Resource<KendaraanByLogistic>>
    suspend fun getKendaraanGudang(): Flow<Resource<List<GudangById>>>
    suspend fun getKendaraanById(id: String): Flow<Resource<Kendaraan>>
    fun getAllKendaraan(
        size: Int = 5,
        filter: String? = null,
        gudang: String? = null,
        status: String? = null,
        search: String? = null,
    ): LiveData<PagingData<AllKendaraan>>
}