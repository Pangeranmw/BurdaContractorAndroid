package com.android.burdacontractor.feature.profile.domain.usecase

import com.android.burdacontractor.feature.profile.domain.repository.IUserRepository
import javax.inject.Inject

class ChangePasswordInteractor @Inject constructor(private val userRepository: IUserRepository) :
    ChangePasswordUseCase {
    override suspend fun execute(
        oldPassword: String,
        newPassword: String,
    ) = userRepository.changePassword(oldPassword, newPassword)
}