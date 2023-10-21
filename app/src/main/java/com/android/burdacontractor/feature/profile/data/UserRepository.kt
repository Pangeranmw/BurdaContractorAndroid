package com.android.burdacontractor.feature.profile.data

import android.util.Log
import com.android.burdacontractor.core.data.Resource
import com.android.burdacontractor.core.data.source.local.StorageDataSource
import com.android.burdacontractor.core.data.source.remote.network.ApiResponse
import com.android.burdacontractor.core.data.source.remote.response.ErrorMessageResponse
import com.android.burdacontractor.feature.profile.data.source.remote.UserRemoteDataSource
import com.android.burdacontractor.feature.profile.data.source.remote.response.UserByTokenItem
import com.android.burdacontractor.feature.profile.domain.repository.IUserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import okhttp3.MultipartBody
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val userRemoteDataSource: UserRemoteDataSource,
    private val storageDataSource: StorageDataSource
) : IUserRepository {

    override suspend fun getUserByToken(): Flow<Resource<UserByTokenItem>> = flow{
        try {
            emit(Resource.Loading())
            when(val response = userRemoteDataSource.getUserByToken(storageDataSource.getToken()).first()){
                is ApiResponse.Empty -> {}
                is ApiResponse.Success -> {
                    val result = response.data.user!!
                    emit(Resource.Success(result))
                }
                is ApiResponse.Error -> emit(Resource.Error(response.errorMessage))
            }
        } catch (ex: Exception) {
            emit(Resource.Error(ex.message.toString()))
            Log.d("getUserByToken", ex.message.toString())
        }
    }

    override suspend fun uploadPhoto(photo: MultipartBody.Part): Flow<Resource<ErrorMessageResponse>> = flow{
        try {
            emit(Resource.Loading())
            when(val response = userRemoteDataSource.uploadPhoto(storageDataSource.getToken(), photo).first()){
                is ApiResponse.Empty -> {}
                is ApiResponse.Success -> {
                    emit(Resource.Success(response.data))
                }
                is ApiResponse.Error -> emit(Resource.Error(response.errorMessage))
            }
        } catch (ex: Exception) {
            emit(Resource.Error(ex.message.toString()))
        }
    }
    override suspend fun uploadTtd(ttd: MultipartBody.Part): Flow<Resource<ErrorMessageResponse>> = flow{
        try {
            emit(Resource.Loading())
            when(val response = userRemoteDataSource.uploadTtd(storageDataSource.getToken(), ttd).first()){
                is ApiResponse.Empty -> {}
                is ApiResponse.Success -> {
                    emit(Resource.Success(response.data))
                }
                is ApiResponse.Error -> emit(Resource.Error(response.errorMessage))
            }
        } catch (ex: Exception) {
            emit(Resource.Error(ex.message.toString()))
        }
    }
}

