package com.android.burdacontractor.feature.logistic.data.source.remote

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.android.burdacontractor.core.data.source.remote.network.ApiResponse
import com.android.burdacontractor.feature.logistic.data.source.remote.network.LogisticService
import com.android.burdacontractor.feature.logistic.data.source.remote.response.GetLogisticByIdResponse
import com.android.burdacontractor.feature.logistic.domain.model.AllLogistic
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LogisticRemoteDataSource @Inject constructor(
    private val logisticService: LogisticService,
) {
    fun getAllLogistic(
        token: String,
        search: String? = null,
        coordinate: String? = null,
        size: Int = 5,
    ): Flow<PagingData<AllLogistic>> {
        return Pager(
            config = PagingConfig(
                pageSize = size
            ),
            pagingSourceFactory = {
                LogisticPagingSource(
                    logisticService,
                    token,
                    search,
                    coordinate,
                    size,
                )
            }
        ).flow
    }

    suspend fun getLogisticById(
        token: String,
        id: String
    ): Flow<ApiResponse<GetLogisticByIdResponse>> = flow {
        val response = logisticService.getLogisticById(token, id)
        if (!response.error) {
            emit(ApiResponse.Success(response))
        } else {
            emit(ApiResponse.Error(response.message))
        }
    }
}

