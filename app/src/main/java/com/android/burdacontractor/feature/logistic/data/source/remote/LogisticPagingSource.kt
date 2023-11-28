package com.android.burdacontractor.feature.logistic.data.source.remote

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.android.burdacontractor.feature.proyek.data.source.remote.network.LogisticService
import com.android.burdacontractor.feature.proyek.domain.model.AllLogistic

class LogisticPagingSource(
    private val logisticService: LogisticService,
    private val token: String,
    private val search: String? = null,
    private val coordinate: String? = null,
    private val size: Int = 5,
) : PagingSource<Int, AllLogistic>() {
    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, AllLogistic> {
        return try {
            val page = params.key ?: INITIAL_PAGE_INDEX
            val response = logisticService.getAllLogistic(
                token, page, size, search, coordinate
            ).logistic

            LoadResult.Page(
                data = response,
                prevKey = if (page == INITIAL_PAGE_INDEX) null else page - 1,
                nextKey = if (response.isEmpty()) null else page + 1
            )
        } catch (exception: Exception) {
            return LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, AllLogistic>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}