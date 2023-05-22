package com.android.burdacontractor.core.domain.usecase

import com.android.burdacontractor.core.data.Resource
import com.android.burdacontractor.core.data.source.remote.response.AddSuratJalanResponse
import com.android.burdacontractor.core.data.source.remote.response.AllSuratJalanResponse
import com.android.burdacontractor.core.data.source.remote.response.ErrorMessageResponse
import com.android.burdacontractor.core.data.source.remote.response.SuratJalanDetailItem
import com.android.burdacontractor.core.data.source.remote.response.SuratJalanDetailResponse
import com.android.burdacontractor.core.data.source.remote.response.SuratJalanItem
import com.android.burdacontractor.core.domain.model.LogisticCoordinate
import com.android.burdacontractor.core.domain.model.Tourism
import com.android.burdacontractor.core.domain.model.enum.SuratJalanStatus
import com.android.burdacontractor.core.domain.model.enum.SuratJalanTipe
import kotlinx.coroutines.flow.Flow
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface LogisticUseCase {
    suspend fun getCoordinate(logisticId: String): Flow<Resource<LogisticCoordinate>>
    suspend fun setCoordinate(logisticId: String, logisticCoordinate: LogisticCoordinate)
}