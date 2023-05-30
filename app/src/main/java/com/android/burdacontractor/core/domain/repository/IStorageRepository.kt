package com.android.burdacontractor.core.domain.repository

import com.android.burdacontractor.core.data.Resource
import com.android.burdacontractor.core.domain.model.LogisticCoordinate
import kotlinx.coroutines.flow.Flow

interface IStorageRepository {
    fun loginUser(userId: String, token: String, role: String)
    fun setDeviceToken(token: String)
    fun getDeviceToken(): String

    fun getUserId(): String

    fun getToken(): String

    fun getLatitude(): String
    fun getLongitude(): String

    fun setCoordinate(latitude: String, longitude: String)

    fun getRole(): String

    fun isUserLogin(): Boolean

    fun logoutUser()
}