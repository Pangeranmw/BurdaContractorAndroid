package com.android.burdacontractor.core.data

import com.android.burdacontractor.core.data.source.local.StorageDataSource
import com.android.burdacontractor.core.domain.repository.IStorageRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StorageRepository @Inject constructor(
    private val storageDataSource: StorageDataSource
) : IStorageRepository {
    override fun loginUser(userId: String, token: String, role: String, ttd: String) {
        storageDataSource.loginUser(userId, token, role, ttd)
    }
    override fun setDeviceToken(token: String) {
        storageDataSource.setDeviceToken(token)
    }
    override fun setTracking(isTracking: Boolean) {
        storageDataSource.setTracking(isTracking)
    }
    override fun getDeviceToken() = storageDataSource.getDeviceToken()
    override fun getUserId() = storageDataSource.getUserId()

    override fun getToken() = storageDataSource.getToken()
    override fun getLatitude() = storageDataSource.getLatitude()

    override fun getLongitude() = storageDataSource.getLongitude()
    override fun setCoordinate(latitude: String, longitude: String) {
        storageDataSource.setCoordinate(latitude,longitude)
    }

    override fun getRole() = storageDataSource.getRole()
    override fun getTTD() = storageDataSource.getTTD()

    override fun setTTD(ttd: String) {
        storageDataSource.setTTD(ttd)
    }
    override fun isUserLogin() = storageDataSource.isUserLogin()
    override fun isTracking() = storageDataSource.isTracking()
    override fun logoutUser() = storageDataSource.logoutUser()
}

