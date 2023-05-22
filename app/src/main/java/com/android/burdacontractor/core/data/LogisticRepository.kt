package com.android.burdacontractor.core.data

import com.android.burdacontractor.core.data.source.local.LocalDataSource
import com.android.burdacontractor.core.data.source.remote.LogisticRemoteDataSource
import com.android.burdacontractor.core.data.source.remote.RemoteDataSource
import com.android.burdacontractor.core.data.source.remote.network.ApiResponse
import com.android.burdacontractor.core.data.source.remote.response.AddSuratJalanResponse
import com.android.burdacontractor.core.data.source.remote.response.ErrorMessageResponse
import com.android.burdacontractor.core.data.source.remote.response.SuratJalanDetailItem
import com.android.burdacontractor.core.data.source.remote.response.SuratJalanItem
import com.android.burdacontractor.core.data.source.remote.response2.TourismResponse
import com.android.burdacontractor.core.domain.model.LogisticCoordinate
import com.android.burdacontractor.core.domain.model.Tourism
import com.android.burdacontractor.core.domain.model.enum.SuratJalanStatus
import com.android.burdacontractor.core.domain.model.enum.SuratJalanTipe
import com.android.burdacontractor.core.domain.repository.ILogisticRepository
import com.android.burdacontractor.core.domain.repository.ISuratJalanRepository
import com.android.burdacontractor.core.domain.repository.ITourismRepository
import com.android.burdacontractor.core.utils.AppExecutors
import com.android.burdacontractor.core.utils.DataMapper
import com.google.firebase.database.DatabaseReference
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LogisticRepository @Inject constructor(
    private val logisticRemoteDataSource: LogisticRemoteDataSource
) : ILogisticRepository {

    override suspend fun getCoordinate(logisticId: String): Flow<Resource<LogisticCoordinate>> = logisticRemoteDataSource.getCoordinate(logisticId)

    override suspend fun setCoordinate(logisticId: String, logisticCoordinate: LogisticCoordinate) {
        logisticRemoteDataSource.setCoordinate(logisticId, logisticCoordinate)
    }

}

