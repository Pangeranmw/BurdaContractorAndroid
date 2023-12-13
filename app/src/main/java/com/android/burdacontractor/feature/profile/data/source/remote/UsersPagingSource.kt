package com.android.burdacontractor.feature.profile.data.source.remote

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.android.burdacontractor.feature.profile.data.source.remote.network.UserService
import com.android.burdacontractor.feature.profile.domain.model.User

class UsersPagingSource(
    private val userService: UserService,
    private val token: String,
    private val size: Int = 5,
    private val search: String? = null,
    private val filter: String? = null,
) : PagingSource<Int, User>() {
    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, User> {
        return try {
            val page = params.key ?: INITIAL_PAGE_INDEX
            val response = userService.getAllUsers(
                token, page, size, search, filter
            ).user

            LoadResult.Page(
                data = response,
                prevKey = if (page == INITIAL_PAGE_INDEX) null else page - 1,
                nextKey = if (response.isEmpty()) null else page + 1
            )
        } catch (exception: Exception) {
            return LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, User>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}