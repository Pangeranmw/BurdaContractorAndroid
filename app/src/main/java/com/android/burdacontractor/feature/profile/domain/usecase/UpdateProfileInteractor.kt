package com.android.burdacontractor.feature.profile.domain.usecase

import com.android.burdacontractor.feature.profile.domain.repository.IUserRepository
import javax.inject.Inject

class UpdateProfileInteractor @Inject constructor(private val userRepository: IUserRepository) :
    UpdateProfileUseCase {
    override suspend fun execute(
        nama: String,
        email: String,
        noHp: String,
    ) = userRepository.updateProfile(nama, email, noHp)
}