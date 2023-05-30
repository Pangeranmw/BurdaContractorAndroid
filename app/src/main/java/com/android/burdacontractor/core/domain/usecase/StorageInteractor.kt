package com.android.burdacontractor.core.domain.usecase

import com.android.burdacontractor.core.data.LogisticRepository
import com.android.burdacontractor.core.data.Resource
import com.android.burdacontractor.core.data.StorageRepository
import com.android.burdacontractor.core.domain.model.LogisticCoordinate
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class StorageInteractor @Inject constructor(private val storageRepository: StorageRepository):
    StorageUseCase {
    override fun setDeviceToken(token: String) {
        storageRepository.setDeviceToken(token)
    }

    override fun setCoordinate(latitude: String, longitude: String) {
        storageRepository.setCoordinate(latitude,longitude)
    }

    override fun getDeviceToken()  = storageRepository.getDeviceToken()

    override fun loginUser(userId: String, token: String, role: String) {
        storageRepository.loginUser(userId, token, role)
    }

    override fun getUserId() = storageRepository.getUserId()

    override fun getToken() = storageRepository.getToken()
    override fun getLatitude() = storageRepository.getLatitude()

    override fun getLongitude() = storageRepository.getLongitude()

    override fun getRole() = storageRepository.getRole()

    override fun isUserLogin() = storageRepository.isUserLogin()

    override fun logoutUser() = storageRepository.logoutUser()
}