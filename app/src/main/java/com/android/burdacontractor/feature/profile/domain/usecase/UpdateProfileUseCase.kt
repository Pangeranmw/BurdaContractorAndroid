package com.android.burdacontractor.feature.profile.domain.usecase

import com.android.burdacontractor.core.data.Resource
import com.android.burdacontractor.feature.profile.domain.model.User
import kotlinx.coroutines.flow.Flow

interface UpdateProfileUseCase {
    suspend fun execute(
        nama: String,
        email: String,
        noHp: String,
    ): Flow<Resource<User>>
}