package com.android.burdacontractor.core.data.source.remote

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.android.burdacontractor.core.data.source.remote.network.SuratJalanService
import com.android.burdacontractor.core.data.source.remote.response.allSjToDomain
import com.android.burdacontractor.core.domain.model.AllSuratJalan

class SuratJalanPagingSource(
    private val suratJalanService: SuratJalanService,
    private val token: String,
    private val tipe: String,
    private val status: String,
    private val date_start: String? = null,
    private val date_end: String? = null,
    private val size: Int = 5,
    private val search: String? = null,
) : PagingSource<Int, AllSuratJalan>() {
    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, AllSuratJalan> {
        return try {
            val page = params.key ?: INITIAL_PAGE_INDEX
            val responseData = suratJalanService.getAllSuratJalan(
                token, tipe, status, date_start, date_end, page, size, search
            ).suratJalan!!.allSjToDomain()
            LoadResult.Page(
                data = responseData,
                prevKey = if (page == INITIAL_PAGE_INDEX) null else page - 1,
                nextKey = if (responseData.isEmpty()) null else page + 1
            )
        } catch (exception: Exception) {
            return LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, AllSuratJalan>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}