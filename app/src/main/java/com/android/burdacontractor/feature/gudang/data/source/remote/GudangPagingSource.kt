package com.android.burdacontractor.feature.gudang.data.source.remote

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.android.burdacontractor.feature.gudang.data.source.remote.network.GudangService
import com.android.burdacontractor.feature.gudang.domain.model.AllGudang

class GudangPagingSource(
    private val gudangService: GudangService,
    private val token: String,
    private val size: Int = 5,
    private val search: String? = null,
    private val filter: String? = null,
    private val coordinate: String? = null,
) : PagingSource<Int, AllGudang>() {
    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, AllGudang> {
        return try {
            val page = params.key ?: INITIAL_PAGE_INDEX
            val response = gudangService.getAllGudang(
                token, page, size, search, filter, coordinate,
            ).gudang

            LoadResult.Page(
                data = response,
                prevKey = if (page == INITIAL_PAGE_INDEX) null else page - 1,
                nextKey = if (response.isEmpty()) null else page + 1
            )
        } catch (exception: Exception) {
            return LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, AllGudang>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}