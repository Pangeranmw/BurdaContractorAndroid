package com.android.burdacontractor.feature.profile.domain.usecase

import com.android.burdacontractor.feature.profile.domain.repository.IUserRepository
import java.io.File
import javax.inject.Inject

class UploadTtdInteractor @Inject constructor(private val userRepository: IUserRepository):
    UploadTtdUseCase
{
    override suspend fun execute(
        ttd: File
    )=userRepository.uploadTtd(ttd)
}