package com.android.burdacontractor.feature.suratjalan.domain.usecase

import com.android.burdacontractor.feature.suratjalan.domain.repository.ISuratJalanRepository
import java.io.File
import javax.inject.Inject

class UploadFotoBuktiSuratJalanInteractor @Inject constructor(private val suratJalanRepository: ISuratJalanRepository) :
    UploadFotoBuktiSuratJalanUseCase {
    override suspend fun execute(id: String, fotoBukti: File) =
        suratJalanRepository.uploadFotoBuktiSuratJalan(id, fotoBukti)
}