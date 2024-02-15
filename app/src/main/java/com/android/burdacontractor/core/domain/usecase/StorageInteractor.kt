package com.android.burdacontractor.core.domain.usecase

import com.android.burdacontractor.core.domain.repository.IStorageRepository
import com.android.burdacontractor.feature.profile.data.source.remote.response.UserByTokenItem
import javax.inject.Inject

class StorageInteractor @Inject constructor(private val storageRepository: IStorageRepository) :
    StorageUseCase {
    override fun getPreferences(key: String) = storageRepository.getPreferences(key)
    override fun setPreferences(key: String, value: String) =
        storageRepository.setPreferences(key, value)

    override fun setTracking(isTracking: Boolean) = storageRepository.setTracking(isTracking)
    override fun loginUser(
        userId: String,
        token: String,
        role: String,
        name: String,
        ttd: String,
        photo: String?
    ) {
        storageRepository.loginUser(userId, token, role, name, ttd, photo)
    }

    override fun updateUser(user: UserByTokenItem) {
        storageRepository.updateUser(user)
    }

    override fun isUserLogin() = storageRepository.isUserLogin()
    override fun getTracking() = storageRepository.getTracking()

    override fun logoutUser() = storageRepository.logoutUser()
}