package com.android.burdacontractor.feature.logistic.domain.usecase

import com.android.burdacontractor.core.data.Resource
import com.android.burdacontractor.core.data.source.remote.response.ErrorMessageResponse
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody

interface UploadTtdUseCase {
    suspend fun execute(
        ttd: MultipartBody.Part
    ): Flow<Resource<ErrorMessageResponse>>
}