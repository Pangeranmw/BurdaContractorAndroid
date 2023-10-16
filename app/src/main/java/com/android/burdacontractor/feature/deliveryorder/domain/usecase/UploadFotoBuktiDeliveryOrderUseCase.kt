package com.android.burdacontractor.feature.deliveryorder.domain.usecase

import com.android.burdacontractor.core.data.Resource
import com.android.burdacontractor.core.data.source.remote.response.ErrorMessageResponse
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import java.io.File

interface UploadFotoBuktiDeliveryOrderUseCase {
    suspend fun execute(
        id: String,
        fotoBukti: File
    ): Flow<Resource<ErrorMessageResponse>>
}