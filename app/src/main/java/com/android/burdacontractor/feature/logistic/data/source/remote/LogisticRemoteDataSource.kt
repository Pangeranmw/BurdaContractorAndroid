package com.android.burdacontractor.feature.logistic.data.source.remote

import android.util.Log
import com.android.burdacontractor.core.data.source.remote.network.ApiResponse
import com.android.burdacontractor.core.data.source.remote.response.ErrorMessageResponse
import com.android.burdacontractor.feature.profile.data.source.remote.network.UserService
import com.android.burdacontractor.feature.profile.data.source.remote.response.GetUserByTokenResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MultipartBody
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LogisticRemoteDataSource @Inject constructor(
    private val userService: UserService,
) {
    suspend fun getUserByToken(token: String): Flow<ApiResponse<GetUserByTokenResponse>> = flow {
        val response = userService.getUserByToken(token)
        Log.d("getUserByToken", response.toString())
        if (!response.error) {
            emit(ApiResponse.Success(response))
        } else {
            emit(ApiResponse.Error(response.message))
        }
    }

    suspend fun uploadTtd(
        token: String,
        ttd: MultipartBody.Part
    ): Flow<ApiResponse<ErrorMessageResponse>> = flow {
        val response = userService.uploadTTD(token, ttd)
        if (!response.error) {
            emit(ApiResponse.Success(response))
        } else {
            emit(ApiResponse.Error(response.message))
        }
    }

    suspend fun uploadPhoto(
        token: String,
        photo: MultipartBody.Part
    ): Flow<ApiResponse<ErrorMessageResponse>> = flow {
        val response = userService.uploadPhoto(token, photo)
        if (!response.error) {
            emit(ApiResponse.Success(response))
        } else {
            emit(ApiResponse.Error(response.message))
        }
    }
}

