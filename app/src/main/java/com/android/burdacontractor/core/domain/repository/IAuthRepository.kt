package com.android.burdacontractor.core.domain.repository

import com.android.burdacontractor.core.data.Resource
import com.android.burdacontractor.core.data.source.remote.response.ErrorMessageResponse
import com.android.burdacontractor.core.data.source.remote.response.LoginResponse
import kotlinx.coroutines.flow.Flow

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