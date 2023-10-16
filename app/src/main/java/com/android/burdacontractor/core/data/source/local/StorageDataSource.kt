package com.android.burdacontractor.core.data.source.local

import com.android.burdacontractor.core.data.source.local.storage.SessionManager
import com.android.burdacontractor.feature.profile.data.source.remote.response.UserByTokenItem
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StorageDataSource @Inject constructor(private val sessionManager: SessionManager) {

    fun loginUser(userId: String, token: String, role: String,  name: String, ttd: String?, photo: String?) {
        sessionManager.createLoginSession(true)
        sessionManager.saveToPreference(SessionManager.KEY_USER_ID, userId)
        sessionManager.saveToPreference(SessionManager.KEY_ROLE, role)
        sessionManager.saveToPreference(SessionManager.KEY_TTD, ttd)
        sessionManager.saveToPreference(SessionManager.KEY_NAME, name)
        sessionManager.saveToPreference(SessionManager.KEY_TOKEN, token)
        sessionManager.saveToPreference(SessionManager.KEY_PHOTO, photo)
        sessionManager.saveToPreference(SessionManager.KEY_PHOTO, photo)
    }
    fun updateUser(user: UserByTokenItem) {
        sessionManager.saveToPreference(SessionManager.KEY_USER_ID, user.id)
        sessionManager.saveToPreference(SessionManager.KEY_ROLE, user.role)
        sessionManager.saveToPreference(SessionManager.KEY_TTD, user.ttd)
        sessionManager.saveToPreference(SessionManager.KEY_PHOTO, user.foto)
    }
    fun getPreferences(key: String) = sessionManager.getFromPreference(key).toString()
    fun setPreferences(key: String, value: String) = sessionManager.saveToPreference(key,value)
    fun setTracking(isTracking: Boolean) = sessionManager.setTracking(isTracking)
    fun getToken() = "Bearer ${sessionManager.getFromPreference(SessionManager.KEY_TOKEN).toString()}"

    fun isUserLogin() = sessionManager.isLogin()
    fun getTracking() = sessionManager.getTracking()
    fun logoutUser() = sessionManager.logout()
}