package com.android.burdacontractor.core.data.source.local

import com.android.burdacontractor.core.data.source.local.entity.TourismEntity
import com.android.burdacontractor.core.data.source.local.room.TourismDao
import com.android.burdacontractor.core.data.source.local.storage.SessionManager
import com.android.burdacontractor.core.domain.model.UserRole
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StorageDataSource @Inject constructor(private val sessionManager: SessionManager) {

    fun loginUser(userId: String, token: String, role: String) {
        sessionManager.createLoginSession()
        sessionManager.saveToPreference(SessionManager.KEY_USER_ID, userId)
        sessionManager.saveToPreference(SessionManager.KEY_ROLE, role)
        sessionManager.saveToPreference(SessionManager.KEY_TOKEN, token)
    }

    fun getUserId() = sessionManager.getFromPreference(SessionManager.KEY_USER_ID).toString()

    fun getToken() = sessionManager.getFromPreference(SessionManager.KEY_TOKEN).toString()

    fun getRole() = sessionManager.getFromPreference(SessionManager.KEY_ROLE).toString()

    fun isUserLogin() = sessionManager.isLogin

    fun logoutUser() = sessionManager.logout()
}