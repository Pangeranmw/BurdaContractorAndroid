package com.android.burdacontractor.feature.auth.data.source

import com.android.burdacontractor.core.data.Resource
import com.android.burdacontractor.core.data.source.local.StorageDataSource
import com.android.burdacontractor.core.data.source.remote.network.ApiResponse
import com.android.burdacontractor.core.data.source.remote.response.ErrorMessageResponse
import com.android.burdacontractor.core.utils.DataMapper
import com.android.burdacontractor.feature.auth.data.source.remote.AuthRemoteDataSource
import com.android.burdacontractor.feature.auth.data.source.remote.response.LoginItem
import com.android.burdacontractor.feature.auth.domain.model.UserLogin
import com.android.burdacontractor.feature.auth.domain.repository.IAuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val authRemoteDataSource: AuthRemoteDataSource,
    private val storageDataSource: StorageDataSource
) : IAuthRepository {

    override suspend fun register(
        nama: String,
        noHp: String,
        email: String,
        password: String,
    ): Flow<Resource<ErrorMessageResponse>> = flow{
        try {
            when(val response = authRemoteDataSource.register(nama,noHp,email,password).first()){
                is ApiResponse.Empty -> {}
                is ApiResponse.Success -> emit(Resource.Success(response.data))
                is ApiResponse.Error -> emit(Resource.Error(response.errorMessage))
            }
        }catch (ex: Exception) {
            emit(Resource.Error(ex.message.toString()))
        }
    }

    override suspend fun login(email: String, password: String): Flow<Resource<LoginItem>> = flow{
        try {
            emit(Resource.Loading())
            when(val response = authRemoteDataSource.login(email,password,storageDataSource.getDeviceToken()).first()){
                is ApiResponse.Empty -> {}
                is ApiResponse.Success -> {
                    val user = response.data.user
                    storageDataSource.loginUser(user.id, user.token, user.role, user.ttd.toString())
                    emit(Resource.Success(user))
                }
                is ApiResponse.Error -> emit(Resource.Error(response.errorMessage))
            }
        } catch (ex: Exception) {
            emit(Resource.Error(ex.message.toString()))
        }
    }

    override suspend fun loginWithPin(pin: String): Flow<Resource<ErrorMessageResponse>> = flow{
        try {
            emit(Resource.Loading())
            when(val response = authRemoteDataSource.loginWithPin(pin).first()){
                is ApiResponse.Empty -> {}
                is ApiResponse.Success -> emit(Resource.Success(response.data))
                is ApiResponse.Error -> emit(Resource.Error(response.errorMessage))
            }
        } catch (ex: Exception) {
            emit(Resource.Error(ex.message.toString()))
        }
    }

    override suspend fun logout(): Flow<Resource<ErrorMessageResponse>> = flow{
        try {
            emit(Resource.Loading())
            when(val response = authRemoteDataSource.logout(storageDataSource.getToken()).first()){
                is ApiResponse.Empty -> {}
                is ApiResponse.Success -> {
                    storageDataSource.logoutUser()
                    emit(Resource.Success(response.data))
                }
                is ApiResponse.Error -> emit(Resource.Error(response.errorMessage))
            }
        } catch (ex: Exception) {
            emit(Resource.Error(ex.message.toString()))
        }
    }
}

