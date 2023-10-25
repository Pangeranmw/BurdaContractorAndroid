package com.android.burdacontractor.feature.kendaraan.data.source.remote

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.android.burdacontractor.feature.kendaraan.data.source.remote.network.KendaraanService
import com.android.burdacontractor.feature.kendaraan.domain.model.AllKendaraan

class KendaraanPagingSource(
    private val kendaraanService: KendaraanService,
    private val token: String,
    private val size: Int = 5,
    private val filter: String? = null,
    private val gudang: String? = null,
    private val status: String? = null,
    private val search: String? = null,
) : PagingSource<Int, AllKendaraan>() {
    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, AllKendaraan> {
        return try {
            val page = params.key ?: INITIAL_PAGE_INDEX
            val response = kendaraanService.getAllKendaraan(
                token, page, size, filter, gudang, status, search
            ).kendaraan

            LoadResult.Page(
                data = response,
                prevKey = if (page == INITIAL_PAGE_INDEX) null else page - 1,
                nextKey = if (response.isEmpty()) null else page + 1
            )
        } catch (exception: Exception) {
            return LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, AllKendaraan>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}