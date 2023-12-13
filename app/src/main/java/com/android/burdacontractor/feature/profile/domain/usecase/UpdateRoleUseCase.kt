package com.android.burdacontractor.feature.profile.domain.usecase

import com.android.burdacontractor.core.data.Resource
import com.android.burdacontractor.core.data.source.remote.response.ErrorMessageResponse
import kotlinx.coroutines.flow.Flow

interface UpdateRoleUseCase {
    suspend fun execute(
        userId: String,
        role: String,
    ): Flow<Resource<ErrorMessageResponse>>
}