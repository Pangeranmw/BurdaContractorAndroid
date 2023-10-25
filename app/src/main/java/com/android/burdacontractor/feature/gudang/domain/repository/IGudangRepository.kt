package com.android.burdacontractor.feature.gudang.domain.repository

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.android.burdacontractor.core.data.Resource
import com.android.burdacontractor.feature.gudang.domain.model.AllGudang
import com.android.burdacontractor.feature.gudang.domain.model.GudangById
import kotlinx.coroutines.flow.Flow

interface IGudangRepository {
    suspend fun getGudangById(id: String): Flow<Resource<GudangById>>
    suspend fun getGudangProvinsi(): Flow<Resource<List<String>>>
    fun getAllGudang(
        size: Int = 5,
        search: String? = null,
        filter: String? = null,
        coordinate: String? = null,
    ): LiveData<PagingData<AllGudang>>
}