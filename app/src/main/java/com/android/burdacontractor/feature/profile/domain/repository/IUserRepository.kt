package com.android.burdacontractor.feature.profile.domain.repository

import com.android.burdacontractor.core.data.Resource
import com.android.burdacontractor.core.data.source.remote.response.ErrorMessageResponse
import com.android.burdacontractor.feature.profile.data.source.remote.response.UserByTokenItem
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody

interface IUserRepository {

    suspend fun getUserByToken(): Flow<Resource<UserByTokenItem>>

    suspend fun uploadTtd(
        ttd: MultipartBody.Part,
    ): Flow<Resource<ErrorMessageResponse>>

    suspend fun uploadPhoto(
        photo: MultipartBody.Part,
    ): Flow<Resource<ErrorMessageResponse>>
}