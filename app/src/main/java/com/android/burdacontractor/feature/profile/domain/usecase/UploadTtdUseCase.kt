package com.android.burdacontractor.feature.profile.domain.usecase

import com.android.burdacontractor.core.data.Resource
import com.android.burdacontractor.feature.profile.domain.model.User
import kotlinx.coroutines.flow.Flow
import java.io.File

interface UploadTtdUseCase {
    suspend fun execute(
        ttd: File
    ): Flow<Resource<User>>
}