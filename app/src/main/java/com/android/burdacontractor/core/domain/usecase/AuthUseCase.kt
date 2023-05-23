package com.android.burdacontractor.core.domain.usecase

import com.android.burdacontractor.core.data.Resource
import com.android.burdacontractor.core.data.source.remote.response.ErrorMessageResponse
import com.android.burdacontractor.core.data.source.remote.response.LoginResponse
import com.android.burdacontractor.core.domain.model.LogisticCoordinate
import kotlinx.coroutines.flow.Flow

interface AuthUseCase {
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