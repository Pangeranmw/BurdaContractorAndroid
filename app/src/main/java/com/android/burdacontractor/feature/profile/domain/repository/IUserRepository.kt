package com.android.burdacontractor.feature.profile.domain.repository

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.android.burdacontractor.core.data.Resource
import com.android.burdacontractor.core.data.source.remote.response.ErrorMessageResponse
import com.android.burdacontractor.feature.profile.data.source.remote.response.UserByTokenItem
import com.android.burdacontractor.feature.profile.domain.model.User
import kotlinx.coroutines.flow.Flow
import java.io.File

interface IUserRepository {

    suspend fun getUserByToken(): Flow<Resource<UserByTokenItem>>

    fun getAllUsers(
        size: Int = 5,
        search: String? = null,
        filter: String? = null,
    ): LiveData<PagingData<User>>

    suspend fun uploadTtd(
        ttd: File,
    ): Flow<Resource<User>>

    suspend fun uploadPhoto(
        photo: File,
    ): Flow<Resource<User>>

    suspend fun updateProfile(
        nama: String,
        email: String,
        noHp: String,
    ): Flow<Resource<User>>

    suspend fun updateRole(
        userId: String,
        role: String,
    ): Flow<Resource<ErrorMessageResponse>>

    suspend fun changePassword(
        oldPassword: String,
        newPassword: String,
    ): Flow<Resource<ErrorMessageResponse>>
}