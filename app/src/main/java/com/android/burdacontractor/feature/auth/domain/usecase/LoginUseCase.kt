package com.android.burdacontractor.feature.auth.domain.usecase

import com.android.burdacontractor.core.data.Resource
import com.android.burdacontractor.feature.auth.data.source.remote.response.LoginItem
import com.android.burdacontractor.feature.auth.domain.model.UserLogin
import kotlinx.coroutines.flow.Flow

interface LoginUseCase {
    suspend fun execute(email: String, password: String): Flow<Resource<LoginItem>>
}