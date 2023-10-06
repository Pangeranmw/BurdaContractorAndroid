package com.android.burdacontractor.feature.deliveryorder.data.source.remote

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.android.burdacontractor.core.utils.DataMapper
import com.android.burdacontractor.feature.deliveryorder.data.source.remote.network.DeliveryOrderService
import com.android.burdacontractor.feature.deliveryorder.data.source.remote.response.DeliveryOrderItem
import com.android.burdacontractor.feature.deliveryorder.domain.model.AllDeliveryOrder

class DeliveryOrderPagingSource(
    private val deliveryOrderService: DeliveryOrderService,
    private val token: String,
    private val status: String,
    private val date_start: String? = null,
    private val date_end: String? = null,
    private val size: Int = 5,
    private val search: String? = null,
) : PagingSource<Int, DeliveryOrderItem>() {
    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, DeliveryOrderItem> {
        return try {
            val page = params.key ?: INITIAL_PAGE_INDEX
            val response = deliveryOrderService.getAllDeliveryOrder(
                token, status, date_start, date_end, page, size, search
            ).deliveryOrder!!

            LoadResult.Page(
                data = response,
                prevKey = if (page == INITIAL_PAGE_INDEX) null else page - 1,
                nextKey = if (response.isEmpty()) null else page + 1
            )
        } catch (exception: Exception) {
            return LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, DeliveryOrderItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}