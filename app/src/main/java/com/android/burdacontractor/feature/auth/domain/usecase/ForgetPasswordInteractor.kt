package com.android.burdacontractor.feature.auth.domain.usecase

import com.android.burdacontractor.core.data.Resource
import com.android.burdacontractor.core.data.source.remote.response.ErrorMessageResponse
import com.android.burdacontractor.feature.auth.domain.repository.IAuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ForgetPasswordInteractor @Inject constructor(private val authRepository: IAuthRepository) :
    ForgetPasswordUseCase {
    override suspend fun execute(email: String): Flow<Resource<ErrorMessageResponse>> =
        authRepository.forgetPassword(email)
}