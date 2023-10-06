package com.android.burdacontractor.core.data.source.local

import com.android.burdacontractor.core.data.source.local.storage.SessionManager
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StorageDataSource @Inject constructor(private val sessionManager: SessionManager) {

    fun loginUser(userId: String, token: String, role: String, ttd: String) {
        sessionManager.saveToPreference(SessionManager.KEY_USER_ID, userId)
        sessionManager.saveToPreference(SessionManager.KEY_ROLE, role)
        sessionManager.saveToPreference(SessionManager.KEY_TTD, ttd)
        sessionManager.saveToPreference(SessionManager.KEY_TOKEN, token)
    }

    fun setDeviceToken(token: String) {
        sessionManager.saveToPreference(SessionManager.KEY_DEVICE_TOKEN, token)
    }
    fun setTracking(isTracking: Boolean) {
        sessionManager.setTracking(isTracking)
    }
    fun getDeviceToken() = sessionManager.getFromPreference(SessionManager.KEY_DEVICE_TOKEN).toString()
    fun getUserId() = sessionManager.getFromPreference(SessionManager.KEY_USER_ID).toString()

    fun getToken() = "Bearer ${sessionManager.getFromPreference(SessionManager.KEY_TOKEN).toString()}"

    fun setCoordinate(latitude: String, longitude: String) {
        sessionManager.saveToPreference(SessionManager.KEY_LATITUDE, latitude)
        sessionManager.saveToPreference(SessionManager.KEY_LONGITUDE, longitude)
    }
    fun getLatitude() = sessionManager.getFromPreference(SessionManager.KEY_LATITUDE).toString()
    fun getLongitude() = sessionManager.getFromPreference(SessionManager.KEY_LONGITUDE).toString()

    fun getRole() = sessionManager.getFromPreference(SessionManager.KEY_ROLE).toString()
    fun getTTD() = sessionManager.getFromPreference(SessionManager.KEY_TTD).toString()
    fun setTTD(ttd: String) {
        sessionManager.saveToPreference(SessionManager.KEY_TTD, ttd)
    }

    fun isUserLogin() = sessionManager.isLogin
    fun isTracking() = sessionManager.isTracking

    fun logoutUser() = sessionManager.logout()
}