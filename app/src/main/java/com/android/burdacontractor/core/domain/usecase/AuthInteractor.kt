package com.android.burdacontractor.core.domain.usecase

import com.android.burdacontractor.core.data.Resource
import com.android.burdacontractor.core.data.source.remote.response.ErrorMessageResponse
import com.android.burdacontractor.feature.auth.domain.model.UserLogin
import com.android.burdacontractor.feature.auth.domain.repository.IAuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AuthInteractor @Inject constructor(private val authRepository: IAuthRepository):
    AuthUseCase {

    override suspend fun register(
        nama: String,
        noHp: String,
        email: String,
        password: String
    ): Flow<Resource<ErrorMessageResponse>> = authRepository.register(nama,noHp,email,password)

    override suspend fun login(email: String, password: String): Flow<Resource<UserLogin>> = authRepository.login(email,password)

    override suspend fun loginWithPin(pin: String): Flow<Resource<ErrorMessageResponse>> = authRepository.loginWithPin(pin)

    override suspend fun logout(): Flow<Resource<ErrorMessageResponse>> = authRepository.logout()

}