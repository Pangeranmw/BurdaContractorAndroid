package com.android.burdacontractor.core.data

import androidx.paging.PagingData
import com.android.burdacontractor.core.data.source.local.StorageDataSource
import com.android.burdacontractor.core.data.source.remote.AuthRemoteDataSource
import com.android.burdacontractor.core.data.source.remote.SuratJalanRemoteDataSource
import com.android.burdacontractor.core.data.source.remote.network.ApiResponse
import com.android.burdacontractor.core.data.source.remote.response.AddSuratJalanResponse
import com.android.burdacontractor.core.data.source.remote.response.ErrorMessageResponse
import com.android.burdacontractor.core.data.source.remote.response.LoginResponse
import com.android.burdacontractor.core.data.source.remote.response.SuratJalanDetailItem
import com.android.burdacontractor.core.data.source.remote.response.SuratJalanItem
import com.android.burdacontractor.core.domain.model.SuratJalanStatus
import com.android.burdacontractor.core.domain.model.SuratJalanTipe
import com.android.burdacontractor.core.domain.repository.IAuthRepository
import com.android.burdacontractor.core.domain.repository.ISuratJalanRepository
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
        when(val response = authRemoteDataSource.register(nama,noHp,email,password).first()){
            is ApiResponse.Empty -> emit(Resource.Loading())
            is ApiResponse.Success -> emit(Resource.Success(response.data))
            is ApiResponse.Error -> emit(Resource.Error(response.errorMessage))
        }
    }

    override suspend fun login(email: String, password: String): Flow<Resource<LoginResponse>> = flow{
        when(val response = authRemoteDataSource.login(email,password).first()){
            is ApiResponse.Empty -> emit(Resource.Loading())
            is ApiResponse.Success -> emit(Resource.Success(response.data))
            is ApiResponse.Error -> emit(Resource.Error(response.errorMessage))
        }
    }

    override suspend fun loginWithPin(pin: String): Flow<Resource<ErrorMessageResponse>> = flow{
        when(val response = authRemoteDataSource.loginWithPin(pin).first()){
            is ApiResponse.Empty -> emit(Resource.Loading())
            is ApiResponse.Success -> emit(Resource.Success(response.data))
            is ApiResponse.Error -> emit(Resource.Error(response.errorMessage))
        }
    }

    override suspend fun logout(): Flow<Resource<ErrorMessageResponse>> = flow{
        when(val response = authRemoteDataSource.logout(storageDataSource.getToken()).first()){
            is ApiResponse.Empty -> emit(Resource.Loading())
            is ApiResponse.Success -> emit(Resource.Success(response.data))
            is ApiResponse.Error -> emit(Resource.Error(response.errorMessage))
        }
    }
}

