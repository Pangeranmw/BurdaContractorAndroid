package com.android.burdacontractor.core.data

import com.android.burdacontractor.core.data.source.local.StorageDataSource
import com.android.burdacontractor.core.domain.repository.IStorageRepository
import com.android.burdacontractor.feature.profile.data.source.remote.response.UserByTokenItem
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StorageRepository @Inject constructor(
    private val storageDataSource: StorageDataSource
) : IStorageRepository {
    override fun loginUser(userId: String, token: String, role: String, name: String, ttd: String, photo: String?) {
        storageDataSource.loginUser(userId, token, role, name, ttd, photo)
    }
    override fun updateUser(user: UserByTokenItem) {
        storageDataSource.updateUser(user)
    }
    override fun getPreferences(key: String) = storageDataSource.getPreferences(key)
    override fun setPreferences(key: String, value: String) = storageDataSource.setPreferences(key,value)
    override fun setTracking(isTracking: Boolean) = storageDataSource.setTracking(isTracking)
    override fun getTracking() = storageDataSource.getTracking()
    override fun isUserLogin() = storageDataSource.isUserLogin()
    override fun logoutUser() = storageDataSource.logoutUser()
}

