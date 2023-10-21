package com.android.burdacontractor.feature.perusahaan.domain.repository

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.android.burdacontractor.core.data.Resource
import com.android.burdacontractor.feature.perusahaan.domain.model.AllPerusahaan
import com.android.burdacontractor.feature.perusahaan.domain.model.PerusahaanById
import kotlinx.coroutines.flow.Flow

interface IPerusahaanRepository {
    suspend fun getPerusahaanById(id: String): Flow<Resource<PerusahaanById>>
    suspend fun getPerusahaanProvinsi(): Flow<Resource<List<String>>>
    fun getAllPerusahaan(
        size: Int = 5,
        search: String? = null,
        filter: String? = null,
        coordinate: String? = null,
    ): LiveData<PagingData<AllPerusahaan>>
}
