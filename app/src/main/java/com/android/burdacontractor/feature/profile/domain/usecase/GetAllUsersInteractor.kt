package com.android.burdacontractor.feature.profile.domain.usecase

import com.android.burdacontractor.feature.profile.domain.repository.IUserRepository
import javax.inject.Inject

class GetAllUsersInteractor @Inject constructor(private val userRepository: IUserRepository) :
    GetAllUsersUseCase {
    override fun execute(
        size: Int,
        search: String?,
        filter: String?,
    ) = userRepository.getAllUsers(size, search, filter)
}