package com.android.burdacontractor.core.domain.usecase

import com.android.burdacontractor.core.data.StorageRepository
import com.android.burdacontractor.core.domain.model.LogisticIsTracking
import javax.inject.Inject

class StorageInteractor @Inject constructor(private val storageRepository: StorageRepository):
    StorageUseCase {
    override fun setDeviceToken(token: String) {
        storageRepository.setDeviceToken(token)
    }

    override fun setCoordinate(latitude: String, longitude: String) {
        storageRepository.setCoordinate(latitude,longitude)
    }
    override fun setTracking(isTracking: Boolean) {
        storageRepository.setTracking(isTracking)
    }

    override fun getDeviceToken()  = storageRepository.getDeviceToken()

    override fun loginUser(userId: String, token: String, role: String, ttd: String) {
        storageRepository.loginUser(userId, token, role, ttd)
    }
    override fun setTTD(ttd: String) {
        storageRepository.setTTD(ttd)
    }
    override fun getUserId() = storageRepository.getUserId()

    override fun getToken() = storageRepository.getToken()
    override fun getLatitude() = storageRepository.getLatitude()
    override fun getTracking() = storageRepository.getTracking()

    override fun getLongitude() = storageRepository.getLongitude()

    override fun getRole() = storageRepository.getRole()
    override fun getTTD() = storageRepository.getTTD()

    override fun isUserLogin() = storageRepository.isUserLogin()

    override fun logoutUser() = storageRepository.logoutUser()
}