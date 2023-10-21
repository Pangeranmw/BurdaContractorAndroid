package com.android.burdacontractor.feature.logistic.domain.usecase

import com.android.burdacontractor.feature.profile.domain.repository.IUserRepository
import javax.inject.Inject

class GetUserByTokenInteractor @Inject constructor(private val userRepository: IUserRepository) :
    GetUserByTokenUseCase {
    override suspend fun execute() = userRepository.getUserByToken()
}