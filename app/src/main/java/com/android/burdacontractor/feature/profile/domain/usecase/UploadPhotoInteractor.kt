package com.android.burdacontractor.feature.profile.domain.usecase

import com.android.burdacontractor.feature.profile.domain.repository.IUserRepository
import java.io.File
import javax.inject.Inject

class UploadPhotoInteractor @Inject constructor(private val userRepository: IUserRepository) :
    UploadPhotoUseCase {
    override suspend fun execute(
        photo: File
    ) = userRepository.uploadPhoto(photo)
}