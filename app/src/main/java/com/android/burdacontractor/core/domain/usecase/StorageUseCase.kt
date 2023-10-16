package com.android.burdacontractor.core.domain.usecase

import com.android.burdacontractor.feature.profile.data.source.remote.response.UserByTokenItem

interface StorageUseCase {
    fun getPreferences(key: String): String
    fun setPreferences(key: String, value: String): Boolean
    fun setTracking(isTracking: Boolean)
    fun loginUser(userId: String, token: String, role: String, name: String, ttd: String, photo: String?)
    fun updateUser(user: UserByTokenItem)
    fun isUserLogin(): Boolean
    fun getTracking(): Boolean
    fun logoutUser()
}