package com.android.burdacontractor.feature.auth.domain.usecase

import com.android.burdacontractor.core.data.Resource
import com.android.burdacontractor.core.data.source.remote.response.ErrorMessageResponse
import com.android.burdacontractor.feature.auth.domain.repository.IAuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LogoutInteractor @Inject constructor(private val authRepository: IAuthRepository):
    LogoutUseCase {
    override suspend fun execute(): Flow<Resource<ErrorMessageResponse>> = authRepository.logout()
}