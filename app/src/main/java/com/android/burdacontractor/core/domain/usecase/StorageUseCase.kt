package com.android.burdacontractor.core.domain.usecase

interface StorageUseCase {
    fun setDeviceToken(token: String)
    fun getDeviceToken(): String
    fun loginUser(userId: String, token: String, role: String, ttd: String)
    fun getUserId(): String

    fun getToken(): String
    fun getLatitude(): String
    fun getLongitude(): String
    fun getRole(): String
    fun getTTD(): String
    fun setCoordinate(latitude: String, longitude: String)
    fun setTTD(ttd: String)
    fun isUserLogin(): Boolean

    fun logoutUser()
}