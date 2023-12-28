package com.android.burdacontractor.feature.auth.domain.usecase

import com.android.burdacontractor.core.data.Resource
import com.android.burdacontractor.core.data.source.remote.response.ErrorMessageResponse
import kotlinx.coroutines.flow.Flow

interface ForgetPasswordUseCase {
    suspend fun execute(email: String): Flow<Resource<ErrorMessageResponse>>
}