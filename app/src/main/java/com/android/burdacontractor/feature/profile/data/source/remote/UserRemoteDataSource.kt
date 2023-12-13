package com.android.burdacontractor.feature.profile.data.source.remote

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.android.burdacontractor.core.data.source.remote.network.ApiResponse
import com.android.burdacontractor.core.data.source.remote.response.ErrorMessageResponse
import com.android.burdacontractor.feature.profile.data.source.remote.network.UserService
import com.android.burdacontractor.feature.profile.data.source.remote.response.GetUserByTokenResponse
import com.android.burdacontractor.feature.profile.data.source.remote.response.UpdateProfileResponse
import com.android.burdacontractor.feature.profile.domain.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRemoteDataSource @Inject constructor(
    private val userService: UserService,
) {
    fun getAllUsers(
        token: String,
        size: Int = 5,
        search: String? = null,
        filter: String? = null,
    ): Flow<PagingData<User>> {
        return Pager(
            config = PagingConfig(
                pageSize = size
            ),
            pagingSourceFactory = {
                UsersPagingSource(userService, token, size, search, filter)
            }
        ).flow
    }

    suspend fun getUserByToken(token: String): Flow<ApiResponse<GetUserByTokenResponse>> = flow {
        val response = userService.getUserByToken(token)
        if (!response.error) {
            emit(ApiResponse.Success(response))
        } else {
            emit(ApiResponse.Error(response.message))
        }
    }

    suspend fun uploadTtd(
        token: String,
        ttd: File
    ): Flow<ApiResponse<UpdateProfileResponse>> = flow {
        val requestImageFile = ttd.asRequestBody("image/png".toMediaType())
        val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
            "ttd",
            ttd.name,
            requestImageFile
        )
        val response = userService.uploadTTD(token, imageMultipart)
        if (!response.error) {
            emit(ApiResponse.Success(response))
        } else {
            emit(ApiResponse.Error(response.message))
        }
    }

    suspend fun uploadPhoto(
        token: String,
        photo: File
    ): Flow<ApiResponse<UpdateProfileResponse>> = flow {
        val requestImageFile = photo.asRequestBody("image/jpg".toMediaType())
        val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
            "foto",
            photo.name,
            requestImageFile
        )
        val response = userService.uploadPhoto(token, imageMultipart)
        if (!response.error) {
            emit(ApiResponse.Success(response))
        } else {
            emit(ApiResponse.Error(response.message))
        }
    }

    suspend fun changePassword(
        token: String,
        oldPassword: String,
        newPassword: String,
    ): Flow<ApiResponse<ErrorMessageResponse>> = flow {
        val response = userService.changePassword(token, oldPassword, newPassword)
        if (!response.error) {
            emit(ApiResponse.Success(response))
        } else {
            emit(ApiResponse.Error(response.message))
        }
    }

    suspend fun updateRole(
        token: String,
        userId: String,
        role: String,
    ): Flow<ApiResponse<ErrorMessageResponse>> = flow {
        val response = userService.updateRole(token, userId, role)
        if (!response.error) {
            emit(ApiResponse.Success(response))
        } else {
            emit(ApiResponse.Error(response.message))
        }
    }

    suspend fun updateProfile(
        token: String,
        nama: String,
        email: String,
        noHp: String,
    ): Flow<ApiResponse<UpdateProfileResponse>> = flow {
        val response = userService.updateProfile(token, nama, email, noHp)
        if (!response.error) {
            emit(ApiResponse.Success(response))
        } else {
            emit(ApiResponse.Error(response.message))
        }
    }
}

