package com.android.burdacontractor.feature.logistic.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.paging.PagingData
import com.android.burdacontractor.core.data.source.local.StorageDataSource
import com.android.burdacontractor.feature.logistic.data.source.remote.LogisticRemoteDataSource
import com.android.burdacontractor.feature.logistic.domain.model.AllLogistic
import com.android.burdacontractor.feature.logistic.domain.repository.ILogisticRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LogisticRepository @Inject constructor(
    private val logisticRemoteDataSource: LogisticRemoteDataSource,
    private val storageDataSource: StorageDataSource
) : ILogisticRepository {
    override fun getAllLogistic(
        search: String?,
        coordinate: String?,
        size: Int
    ): LiveData<PagingData<AllLogistic>> =
        logisticRemoteDataSource.getAllLogistic(
            storageDataSource.getToken(), search, coordinate, size
        ).asLiveData()
}

