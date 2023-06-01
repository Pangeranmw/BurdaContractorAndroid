package com.android.burdacontractor.feature.auth.domain.usecase

import com.android.burdacontractor.core.data.Resource
import com.android.burdacontractor.core.data.source.remote.response.ErrorMessageResponse
import com.android.burdacontractor.feature.auth.data.source.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RegisterInteractor @Inject constructor(private val authRepository: AuthRepository):
    RegisterUseCase {
    override suspend fun execute(
        nama: String,
        noHp: String,
        email: String,
        password: String
    ): Flow<Resource<ErrorMessageResponse>> =
        authRepository.register(nama,noHp,email,password)
}