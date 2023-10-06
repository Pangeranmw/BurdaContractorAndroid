package com.android.burdacontractor.core.domain.repository

interface IStorageRepository {
    fun loginUser(userId: String, token: String, role: String, ttd: String)
    fun setDeviceToken(token: String)
    fun setTracking(isTracking: Boolean)
    fun setTTD(ttd: String)
    fun getDeviceToken(): String

    fun getUserId(): String

    fun getToken(): String

    fun getLatitude(): String
    fun getLongitude(): String

    fun setCoordinate(latitude: String, longitude: String)

    fun getRole(): String
    fun getTTD(): String

    fun isUserLogin(): Boolean
    fun isTracking(): Boolean
    fun logoutUser()
}