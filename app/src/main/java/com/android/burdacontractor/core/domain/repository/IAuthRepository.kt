package com.android.burdacontractor.core.domain.repository

import androidx.paging.PagingData
import com.android.burdacontractor.core.data.Resource
import com.android.burdacontractor.core.data.source.remote.response.AddSuratJalanResponse
import com.android.burdacontractor.core.data.source.remote.response.ErrorMessageResponse
import com.android.burdacontractor.core.data.source.remote.response.LoginResponse
import com.android.burdacontractor.core.data.source.remote.response.SuratJalanDetailItem
import com.android.burdacontractor.core.data.source.remote.response.SuratJalanItem
import com.android.burdacontractor.core.domain.model.SuratJalanStatus
import com.android.burdacontractor.core.domain.model.SuratJalanTipe
import kotlinx.coroutines.flow.Flow
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface IAuthRepository {

    suspend fun register(
        nama: String,
        noHp: String,
        email: String,
        password: String,
    ): Flow<Resource<ErrorMessageResponse>>

    suspend fun login(email: String, password: String): Flow<Resource<LoginResponse>>

    suspend fun loginWithPin(pin: String): Flow<Resource<ErrorMessageResponse>>

    suspend fun logout(): Flow<Resource<ErrorMessageResponse>>
}