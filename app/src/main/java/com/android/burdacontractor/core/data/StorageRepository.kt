package com.android.burdacontractor.core.data

import com.android.burdacontractor.core.data.source.local.StorageDataSource
import com.android.burdacontractor.core.data.source.local.storage.SessionManager
import com.android.burdacontractor.core.domain.model.LogisticCoordinate
import com.android.burdacontractor.core.domain.repository.IStorageRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StorageRepository @Inject constructor(
    private val storageDataSource: StorageDataSource
) : IStorageRepository {
    override fun loginUser(userId: String, token: String, role: String) {
        storageDataSource.loginUser(userId, token, role)
    }

    override fun getUserId() = storageDataSource.getUserId()

    override fun getToken() = storageDataSource.getToken()

    override fun getRole() = storageDataSource.getRole()

    override fun isUserLogin() = storageDataSource.isUserLogin()

    override fun logoutUser() = storageDataSource.logoutUser()
}

