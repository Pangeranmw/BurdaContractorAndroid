package com.android.burdacontractor.feature.auth.domain.usecase

import com.android.burdacontractor.core.data.Resource
import com.android.burdacontractor.feature.auth.data.source.AuthRepository
import com.android.burdacontractor.feature.auth.domain.model.UserLogin
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LoginInteractor @Inject constructor(private val authRepository: AuthRepository):
    LoginUseCase {
    override suspend fun execute(email: String, password: String): Flow<Resource<UserLogin>> = authRepository.login(email,password)
}