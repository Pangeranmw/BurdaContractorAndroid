package com.android.burdacontractor.feature.perusahaan.data.source.remote

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.android.burdacontractor.feature.perusahaan.data.source.remote.network.PerusahaanService
import com.android.burdacontractor.feature.perusahaan.domain.model.AllPerusahaan

class PerusahaanPagingSource(
    private val perusahaanService: PerusahaanService,
    private val token: String,
    private val size: Int = 5,
    private val search: String? = null,
    private val filter: String? = null,
    private val coordinate: String? = null,
) : PagingSource<Int, AllPerusahaan>() {
    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, AllPerusahaan> {
        return try {
            val page = params.key ?: INITIAL_PAGE_INDEX
            val response = perusahaanService.getAllPerusahaan(
                token, page, size, search, filter, coordinate,
            ).perusahaan

            LoadResult.Page(
                data = response,
                prevKey = if (page == INITIAL_PAGE_INDEX) null else page - 1,
                nextKey = if (response.isEmpty()) null else page + 1
            )
        } catch (exception: Exception) {
            return LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, AllPerusahaan>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}