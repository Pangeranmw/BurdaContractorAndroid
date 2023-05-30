package com.android.burdacontractor.core.data.source.remote

import android.util.Log
import com.android.burdacontractor.core.data.source.remote.network.ApiResponse
import com.android.burdacontractor.core.data.source.remote.network.AuthService
import com.android.burdacontractor.core.data.source.remote.response.ErrorMessageResponse
import com.android.burdacontractor.core.data.source.remote.response.LoginResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
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
        val response = authService.register(nama,noHp,email,password)
        if(!response.error){
            emit(ApiResponse.Success(response))
        }else{
            emit(ApiResponse.Error(response.message))
        }
    }

    suspend fun login(email: String, password: String, deviceToken: String): Flow<ApiResponse<LoginResponse>> = flow{
        val response = authService.login(email,password, deviceToken)
        if(!response.error){
            emit(ApiResponse.Success(response))
        }else{
            emit(ApiResponse.Error(response.message))
        }
    }

    suspend fun loginWithPin(pin: String): Flow<ApiResponse<ErrorMessageResponse>> = flow{
        val response = authService.loginWithPin(pin)
        if(!response.error){
            emit(ApiResponse.Success(response))
        }else{
            emit(ApiResponse.Error(response.message))
        }
    }

    suspend fun logout(token: String): Flow<ApiResponse<ErrorMessageResponse>> = flow{
        val response = authService.logout(token)
        if(!response.error){
            emit(ApiResponse.Success(response))
        }else{
            emit(ApiResponse.Error(response.message))
        }
    }
}

