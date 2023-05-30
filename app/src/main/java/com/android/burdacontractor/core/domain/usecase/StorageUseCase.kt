package com.android.burdacontractor.core.domain.usecase

import com.android.burdacontractor.core.data.Resource
import com.android.burdacontractor.core.domain.model.LogisticCoordinate
import kotlinx.coroutines.flow.Flow

interface StorageUseCase {
    fun setDeviceToken(token: String)
    fun getDeviceToken(): String

    fun loginUser(userId: String, token: String, role: String)

    fun getUserId(): String

    fun getToken(): String
    fun getLatitude(): String
    fun getLongitude(): String
    fun getRole(): String
    fun setCoordinate(latitude: String, longitude: String)

    fun isUserLogin(): Boolean

    fun logoutUser()
}