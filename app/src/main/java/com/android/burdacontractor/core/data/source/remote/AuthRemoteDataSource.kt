package com.android.burdacontractor.core.data.source.remote

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.android.burdacontractor.core.data.Resource
import com.android.burdacontractor.core.data.source.local.StorageDataSource
import com.android.burdacontractor.core.data.source.remote.network.ApiResponse
import com.android.burdacontractor.core.data.source.remote.network.AuthService
import com.android.burdacontractor.core.data.source.remote.network.SuratJalanService
import com.android.burdacontractor.core.data.source.remote.response.AddSuratJalanResponse
import com.android.burdacontractor.core.data.source.remote.response.ErrorMessageResponse
import com.android.burdacontractor.core.data.source.remote.response.LoginResponse
import com.android.burdacontractor.core.data.source.remote.response.SuratJalanDetailItem
import com.android.burdacontractor.core.data.source.remote.response.SuratJalanItem
import com.android.burdacontractor.core.domain.model.SuratJalanStatus
import com.android.burdacontractor.core.domain.model.SuratJalanTipe
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRemoteDataSource @Inject constructor(
    private val authService: AuthService,
) {
    suspend fun register(
        nama: String,
        noHp: String,
        email: String,
        password: String,
    ): Flow<ApiResponse<ErrorMessageResponse>> = flow{
        emit(ApiResponse.Empty)
        val response = authService.register(nama,noHp,email,password)
        if(!response.error){
            emit(ApiResponse.Success(response))
        }else{
            emit(ApiResponse.Error(response.message))
        }
    }

    suspend fun login(email: String, password: String): Flow<ApiResponse<LoginResponse>> = flow{
        emit(ApiResponse.Empty)
        val response = authService.login(email,password)
        if(!response.error){
            emit(ApiResponse.Success(response))
        }else{
            emit(ApiResponse.Error(response.message))
        }
    }

    suspend fun loginWithPin(pin: String): Flow<ApiResponse<ErrorMessageResponse>> = flow{
        emit(ApiResponse.Empty)
        val response = authService.loginWithPin(pin)
        if(!response.error){
            emit(ApiResponse.Success(response))
        }else{
            emit(ApiResponse.Error(response.message))
        }
    }

    suspend fun logout(token: String): Flow<ApiResponse<ErrorMessageResponse>> = flow{
        emit(ApiResponse.Empty)
        val response = authService.logout(token)
        if(!response.error){
            emit(ApiResponse.Success(response))
        }else{
            emit(ApiResponse.Error(response.message))
        }
    }
}

