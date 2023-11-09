package com.android.burdacontractor.feature.suratjalan.data.source.remote

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.android.burdacontractor.core.domain.model.enums.CreatedByOrFor
import com.android.burdacontractor.feature.suratjalan.data.source.remote.network.SuratJalanService
import com.android.burdacontractor.feature.suratjalan.domain.model.AllSuratJalan

class SuratJalanPagingSource(
    private val suratJalanService: SuratJalanService,
    private val token: String,
    private val tipe: String,
    private val status: String,
    private val dateStart: String? = null,
    private val dateEnd: String? = null,
    private val size: Int = 5,
    private val search: String? = null,
    private val createdByOrFor: CreatedByOrFor,
) : PagingSource<Int, AllSuratJalan>() {
    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, AllSuratJalan> {
        return try {
            val page = params.key ?: INITIAL_PAGE_INDEX
            val response = suratJalanService.getAllSuratJalan(
                token, tipe, status, dateStart, dateEnd, page, size, search, createdByOrFor.name
            ).suratJalan!!

            LoadResult.Page(
                data = response,
                prevKey = if (page == INITIAL_PAGE_INDEX) null else page - 1,
                nextKey = if (response.isEmpty()) null else page + 1
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