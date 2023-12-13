package com.android.burdacontractor.feature.profile.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.paging.PagingData
import com.android.burdacontractor.core.data.Resource
import com.android.burdacontractor.core.data.source.local.StorageDataSource
import com.android.burdacontractor.core.data.source.remote.network.ApiResponse
import com.android.burdacontractor.core.data.source.remote.response.ErrorMessageResponse
import com.android.burdacontractor.core.utils.DataMapper
import com.android.burdacontractor.feature.profile.data.source.remote.UserRemoteDataSource
import com.android.burdacontractor.feature.profile.data.source.remote.response.UserByTokenItem
import com.android.burdacontractor.feature.profile.domain.model.User
import com.android.burdacontractor.feature.profile.domain.repository.IUserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val userRemoteDataSource: UserRemoteDataSource,
    private val storageDataSource: StorageDataSource
) : IUserRepository {

    override fun getAllUsers(
        size: Int,
        search: String?,
        filter: String?,
    ): LiveData<PagingData<User>> =
        userRemoteDataSource.getAllUsers(
            storageDataSource.getToken(), size, search, filter
        ).asLiveData()

    override suspend fun getUserByToken(): Flow<Resource<UserByTokenItem>> =
        flow {
            emit(Resource.Loading())
            when (val response =
                userRemoteDataSource.getUserByToken(storageDataSource.getToken()).first()) {
                is ApiResponse.Empty -> {}
                is ApiResponse.Success -> {
                    val result = response.data.user!!
                    emit(Resource.Success(result, response.data.message))
                }
                is ApiResponse.Error -> emit(Resource.Error(response.errorMessage))
            }
        }.catch {
            emit(Resource.Error(it.message.toString()))
        }.flowOn(Dispatchers.IO)

    override suspend fun uploadPhoto(photo: File): Flow<Resource<User>> =
        flow {
            emit(Resource.Loading())
            when (val response =
                userRemoteDataSource.uploadPhoto(storageDataSource.getToken(), photo).first()) {
                is ApiResponse.Empty -> {}
                is ApiResponse.Success -> {
                    storageDataSource.updateUser(DataMapper.userToUserByToken(response.data.user))
                    emit(Resource.Success(response.data.user, response.data.message))
                }

                is ApiResponse.Error -> emit(Resource.Error(response.errorMessage))
            }
        }.catch {
            emit(Resource.Error(it.message.toString()))
        }.flowOn(Dispatchers.IO)

    override suspend fun updateRole(
        userId: String,
        role: String
    ): Flow<Resource<ErrorMessageResponse>> =
        flow {
            emit(Resource.Loading())
            when (val response =
                userRemoteDataSource.updateRole(storageDataSource.getToken(), userId, role)
                    .first()) {
                is ApiResponse.Empty -> {}
                is ApiResponse.Success -> {
                    emit(Resource.Success(response.data, response.data.message))
                }

                is ApiResponse.Error -> emit(Resource.Error(response.errorMessage))
            }
        }.catch {
            emit(Resource.Error(it.message.toString()))
        }.flowOn(Dispatchers.IO)

    override suspend fun updateProfile(
        nama: String,
        email: String,
        noHp: String
    ): Flow<Resource<User>> =
        flow {
            emit(Resource.Loading())
            when (val response =
                userRemoteDataSource.updateProfile(storageDataSource.getToken(), nama, email, noHp)
                    .first()) {
                is ApiResponse.Empty -> {}
                is ApiResponse.Success -> {
                    storageDataSource.updateUser(DataMapper.userToUserByToken(response.data.user))
                    emit(Resource.Success(response.data.user, response.data.message))
                }

                is ApiResponse.Error -> emit(Resource.Error(response.errorMessage))
            }
        }.catch {
            emit(Resource.Error(it.message.toString()))
        }.flowOn(Dispatchers.IO)

    override suspend fun changePassword(
        oldPassword: String,
        newPassword: String
    ): Flow<Resource<ErrorMessageResponse>> =
        flow {
            emit(Resource.Loading())
            when (val response =
                userRemoteDataSource.changePassword(
                    storageDataSource.getToken(),
                    oldPassword,
                    newPassword
                ).first()) {
                is ApiResponse.Empty -> {}
                is ApiResponse.Success -> {
                    emit(Resource.Success(response.data, response.data.message))
                }

                is ApiResponse.Error -> emit(Resource.Error(response.errorMessage))
            }
        }.catch {
            emit(Resource.Error(it.message.toString()))
        }.flowOn(Dispatchers.IO)

    override suspend fun uploadTtd(ttd: File): Flow<Resource<User>> =
        flow {
            emit(Resource.Loading())
            when (val response =
                userRemoteDataSource.uploadTtd(storageDataSource.getToken(), ttd).first()) {
                is ApiResponse.Empty -> {}
                is ApiResponse.Success -> {
                    storageDataSource.updateUser(DataMapper.userToUserByToken(response.data.user))
                    emit(Resource.Success(response.data.user, response.data.message))
                }

                is ApiResponse.Error -> emit(Resource.Error(response.errorMessage))
            }
        }.catch {
            emit(Resource.Error(it.message.toString()))
        }.flowOn(Dispatchers.IO)
}

