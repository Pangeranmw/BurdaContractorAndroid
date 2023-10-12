package com.android.burdacontractor.core.data

import androidx.paging.PagingData
import com.android.burdacontractor.core.data.Resource
import com.android.burdacontractor.core.data.source.local.StorageDataSource
import com.android.burdacontractor.core.data.source.remote.OSRMRemoteDataSource
import com.android.burdacontractor.core.data.source.remote.network.ApiResponse
import com.android.burdacontractor.core.data.source.remote.response.CountActiveResponse
import com.android.burdacontractor.core.data.source.remote.response.DistanceMatrixResponse
import com.android.burdacontractor.core.data.source.remote.response.ErrorMessageResponse
import com.android.burdacontractor.core.domain.model.CountActive
import com.android.burdacontractor.core.domain.model.enums.SuratJalanStatus
import com.android.burdacontractor.core.domain.model.enums.SuratJalanTipe
import com.android.burdacontractor.core.domain.repository.IOSRMRepository
import com.android.burdacontractor.core.utils.DataMapper
import com.android.burdacontractor.feature.suratjalan.data.source.remote.SuratJalanRemoteDataSource
import com.android.burdacontractor.feature.suratjalan.data.source.remote.response.DataAllSuratJalanWithCountItem
import com.android.burdacontractor.feature.suratjalan.data.source.remote.response.SuratJalanDetailItem
import com.android.burdacontractor.feature.suratjalan.data.source.remote.response.SuratJalanItem
import com.android.burdacontractor.feature.suratjalan.domain.model.AllSuratJalan
import com.android.burdacontractor.feature.suratjalan.domain.model.DataAllSuratJalanWithCount
import com.android.burdacontractor.feature.suratjalan.domain.model.SuratJalanDetail
import com.android.burdacontractor.feature.suratjalan.domain.repository.ISuratJalanRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OSRMRepository @Inject constructor(
    private val osrmDataSource: OSRMRemoteDataSource,
) : IOSRMRepository {
    override suspend fun getDistanceMatrix(coordinates: String): Flow<Resource<DistanceMatrixResponse>> = flow{
        try {
            emit(Resource.Loading())
            when(val response = osrmDataSource.getDistanceMatrix(coordinates).first()){
                is ApiResponse.Empty -> {}
                is ApiResponse.Success -> {
                    val result = response.data
                    emit(Resource.Success(result))
                }
                is ApiResponse.Error -> emit(Resource.Error(response.errorMessage))
            }
        } catch (ex: Exception) {
            emit(Resource.Error(ex.message.toString()))
        }
    }
}

