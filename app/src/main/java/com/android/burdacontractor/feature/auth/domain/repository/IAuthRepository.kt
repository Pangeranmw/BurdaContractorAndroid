package com.android.burdacontractor.feature.auth.domain.repository

import com.android.burdacontractor.core.data.Resource
import com.android.burdacontractor.core.data.source.remote.response.ErrorMessageResponse
import com.android.burdacontractor.feature.auth.data.source.remote.response.LoginItem
import kotlinx.coroutines.flow.Flow

interface IAuthRepository {

    suspend fun register(
        nama: String,
        noHp: String,
        email: String,
        password: String,
    ): Flow<Resource<ErrorMessageResponse>>

    suspend fun login(email: String, password: String): Flow<Resource<LoginItem>>
    suspend fun forgetPassword(email: String): Flow<Resource<ErrorMessageResponse>>

    suspend fun loginWithPin(pin: String): Flow<Resource<ErrorMessageResponse>>

    suspend fun logout(): Flow<Resource<ErrorMessageResponse>>
}