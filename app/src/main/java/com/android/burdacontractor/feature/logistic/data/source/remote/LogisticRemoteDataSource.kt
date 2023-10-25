package com.android.burdacontractor.feature.logistic.data.source.remote

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.android.burdacontractor.feature.logistic.data.source.remote.network.LogisticService
import com.android.burdacontractor.feature.logistic.domain.model.AllLogistic
import kotlinx.coroutines.flow.Flow
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
}

