package com.android.burdacontractor.feature.logistic.domain.usecase

import com.android.burdacontractor.core.data.Resource
import com.android.burdacontractor.feature.profile.data.source.remote.response.UserByTokenItem
import kotlinx.coroutines.flow.Flow

interface GetUserByTokenUseCase {
    suspend fun execute(): Flow<Resource<UserByTokenItem>>
}