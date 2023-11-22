package com.android.burdacontractor.feature.suratjalan.domain.usecase

import com.android.burdacontractor.core.data.Resource
import com.android.burdacontractor.core.data.source.remote.response.ErrorMessageResponse
import kotlinx.coroutines.flow.Flow
import java.io.File

interface UploadFotoBuktiSuratJalanUseCase {
    suspend fun execute(
        id: String,
        fotoBukti: File,
    ): Flow<Resource<ErrorMessageResponse>>
}