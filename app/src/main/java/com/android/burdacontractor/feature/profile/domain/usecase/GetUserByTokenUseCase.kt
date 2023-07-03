package com.android.burdacontractor.feature.profile.domain.usecase

import com.android.burdacontractor.core.data.Resource
import com.android.burdacontractor.core.domain.model.User
import kotlinx.coroutines.flow.Flow

interface GetUserByTokenUseCase {
    suspend fun execute(): Flow<Resource<User>>
}