package com.android.burdacontractor.feature.profile.domain.usecase

import com.android.burdacontractor.feature.profile.domain.repository.IUserRepository
import javax.inject.Inject

class UpdateRoleInteractor @Inject constructor(private val userRepository: IUserRepository) :
    UpdateRoleUseCase {
    override suspend fun execute(
        userId: String,
        role: String,
    ) = userRepository.updateRole(userId, role)
}